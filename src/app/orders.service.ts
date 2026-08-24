import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, signal, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { catchError, concatMap, map, Observable, of } from 'rxjs';
import { environment } from '../environments/environment';
import { MenuItem } from './pages/menus/menu-data';
import { PaymentService } from './payment.service';

export type TrackingStage = 'preparing' | 'out-for-delivery' | 'delivered' | 'cancelled';

export interface OrderLine {
  item: MenuItem;
  quantity: number;
}

export interface PlaceBackendOrderRequest {
  customerName: string;
  customerPhone: string;
  customerAddress: string;
  restaurantId: string;
  itemIds: string[];
  totalAmt: number;
  paymentMethod: 'cod' | 'upi' | 'card';
}


export interface TrackedOrder {
  orderNumber: string;
  restaurant: string;
  placedOn: string;
  trackingStage: TrackingStage;
  lines: OrderLine[];
  deliveryFee: number;
  customerName: string;
  deliveryAddress: string;
  estimatedArrival?: string;
  assignedAgent?: string;
  customerPhone?: string;
}

@Injectable({ providedIn: 'root' })
export class OrdersService {
  private readonly apiUrl = environment.apiUrl;
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));

  constructor(private http: HttpClient, private paymentService: PaymentService) {
    if (this.isBrowser) {
      this.refresh();
    }
  }
  private readonly ordersSignal = signal<TrackedOrder[]>([]);
  readonly orders = this.ordersSignal.asReadonly();

  /** Fetch current orders from backend and update local signal. */
  refresh(): void {
    this.http
      .get<any[]>(`${this.apiUrl}/orders/all`)
      .pipe(catchError(() => of([] as any[])))
      .subscribe((backendOrders) => {
        const tracked: TrackedOrder[] = backendOrders.map((o) => {
          const restaurantName = o.restaurant?.resName ?? o.restaurant?.resId ?? '';
          const trackingStage: TrackedOrder['trackingStage'] = (() => {
            // Match on the exact backend status values (PLACED, OUT_FOR_DELIVERY,
            // DELIVERED, CANCELLED) rather than loose substring checks - "out for
            // delivery" also contains the substring "deliver", so a naive
            // deliver-before-out check would misclassify it as delivered.
            const s = (o.orderStatus || '').toString().trim().toUpperCase();
            if (s === 'DELIVERED') return 'delivered';
            if (s === 'CANCELLED') return 'cancelled';
            if (s === 'OUT_FOR_DELIVERY') return 'out-for-delivery';
            return 'preparing';
          })();

          const lines: OrderLine[] = (o.menus || []).map((m: any) => ({
            quantity: 1,
            item: {
              itemId: m.itemId ?? m.itemId,
              resId: m.restaurant?.resId ?? o.restaurant?.resId ?? '',
              name: m.itemName ?? m.itemName,
              description: m.description ?? m.description,
              price: m.price ?? m.price,
              veg: true,
              icon: '🍽️',
            } as MenuItem,
          }));

          const trackedOrder: TrackedOrder = {
            orderNumber: o.orderId,
            restaurant: restaurantName,
            placedOn: new Date().toLocaleString(),
            trackingStage,
            lines,
            deliveryFee: 0,
            customerName: o.customer?.customerName ?? o.customer?.customerId ?? 'Unknown',
            deliveryAddress: o.customer?.customerAddress ?? '',
            estimatedArrival: undefined,
            assignedAgent: undefined,
            customerPhone: o.customer?.customerPhoneNo ?? undefined,
          };
          return trackedOrder;
        });

        this.ordersSignal.set(tracked);
      });
  }

  addOrder(order: TrackedOrder): void {
    this.ordersSignal.update((orders) => [order, ...orders]);
  }

  /**
   * Best-effort sync of a placed order to the Spring Boot backend: upserts a Customer,
   * creates the Order (linked to the restaurant and distinct menu items) and records a Payment.
   * Errors are swallowed so the local order flow always succeeds even if the backend is unreachable.
   */
  placeBackendOrder(request: PlaceBackendOrderRequest): Observable<string | null> {
    const customerId = 'CUST' + String(Math.abs(this.hash(request.customerPhone || request.customerName)) % 1_000_000).padStart(6, '0');
    // Backend orderId column is varchar(10); keep the generated id within that limit.
    const orderId = 'ORD' + Date.now().toString().slice(-7);

    const customerPayload = {
      customerId,
      customerName: request.customerName || 'Guest',
      customerEmail: `${customerId.toLowerCase()}@krustykrab.com`,
      customerPhoneNo: (request.customerPhone || '9000000000').replace(/\D/g, '').slice(-10).padStart(10, '9'),
      customerAddress: request.customerAddress || 'Not provided',
    };

    const orderPayload = {
      orderId,
      customer: { customerId },
      orderStatus: 'PLACED',
      totalAmt: request.totalAmt,
      restaurant: { resId: request.restaurantId },
      menus: request.itemIds.map((itemId) => ({ itemId })),
    };

    const payMethodMap: Record<PlaceBackendOrderRequest['paymentMethod'], 'UPI' | 'CARD' | 'CASH'> = {
      upi: 'UPI',
      card: 'CARD',
      cod: 'CASH',
    };

    return this.http.post(`${this.apiUrl}/api/customers`, customerPayload).pipe(
      concatMap(() => this.http.post(`${this.apiUrl}/orders/add`, orderPayload)),
      concatMap(() =>
        this.paymentService.makePayment({
          payId: 'PAY' + Date.now(),
          orderId,
          payMethod: payMethodMap[request.paymentMethod],
          payAmt: request.totalAmt,
          payStatus: request.paymentMethod === 'cod' ? 'PENDING' : 'SUCCESS',
        })
      ),
      map(() => orderId),
      catchError((err) => {
        console.error('Backend order sync failed; order was only saved locally.', err);
        return of(null);
      })
    );
  }

  private hash(value: string): number {
    let hash = 0;
    for (let i = 0; i < value.length; i++) {
      hash = (hash << 5) - hash + value.charCodeAt(i);
      hash |= 0;
    }
    return hash;
  }

  getByNumber(orderNumber: string): TrackedOrder | undefined {
    return this.ordersSignal().find((order) => order.orderNumber === orderNumber);
  }
}
