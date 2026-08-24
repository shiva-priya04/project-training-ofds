import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, PLATFORM_ID, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable, catchError, map, of } from 'rxjs';
import { environment } from '../environments/environment';

export type DeliveryStatus = 'ASSIGNED' | 'ACCEPTED' | 'DECLINED' | 'OUT_FOR_DELIVERY' | 'DELIVERED';

interface BackendOrderItem {
  itemId: string;
  itemName: string;
  description?: string;
  price: number;
}

interface BackendDeliveryOrder {
  orderId: string;
  orderStatus?: string;
  totalAmt?: number;
  customer?: { customerId: string; customerName?: string; customerPhoneNo?: string; customerAddress?: string };
  restaurant?: { resId: string; resName?: string };
  menus?: BackendOrderItem[];
}

export interface Delivery {
  delId: string;
  delStatus: DeliveryStatus;
  estimatedTimeOfArrival: string;
  agent: { agentId: string; agentName?: string };
  order: BackendDeliveryOrder;
}

export interface NewDelivery {
  orderId: string;
  agentId: string;
  estimatedTimeOfArrival: string;
}

@Injectable({ providedIn: 'root' })
export class DeliveryService {
  private readonly apiUrl = environment.apiUrl;
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));
  private readonly deliveriesSignal = signal<Delivery[]>([]);
  readonly deliveries = this.deliveriesSignal.asReadonly();

  constructor(private http: HttpClient) {
    if (this.isBrowser) {
      this.refresh();
    }
  }

  refresh(): void {
    this.http
      .get<Delivery[]>(`${this.apiUrl}/delivery`)
      .pipe(catchError(() => of([] as Delivery[])))
      .subscribe((deliveries) => this.deliveriesSignal.set(deliveries));
  }

  /** Fetch only the deliveries assigned to a specific agent. */
  refreshForAgent(agentId: string): void {
    this.http
      .get<Delivery[]>(`${this.apiUrl}/delivery/agent/${agentId}`)
      .pipe(catchError(() => of([] as Delivery[])))
      .subscribe((deliveries) => this.deliveriesSignal.set(deliveries));
  }

  /** Assigns an order to an agent, creating a Delivery record in the backend. */
  assignDelivery(data: NewDelivery): Observable<string | null> {
    const payload = {
      delId: 'DEL' + Date.now(),
      delStatus: 'ASSIGNED',
      estimatedTimeOfArrival: data.estimatedTimeOfArrival,
      agent: { agentId: data.agentId },
      order: { orderId: data.orderId },
    };
    return this.http.post<Delivery>(`${this.apiUrl}/delivery`, payload).pipe(
      map(() => {
        this.refresh();
        return null;
      }),
      catchError((err: HttpErrorResponse) => of(this.toErrorMessage(err)))
    );
  }

  /** Updates a delivery's status. Also updates the linked order's status on the backend (except DECLINED). */
  updateStatus(delId: string, status: DeliveryStatus, agentId?: string): Observable<string | null> {
    return this.http.patch<Delivery>(`${this.apiUrl}/delivery/${delId}/status`, { status }).pipe(
      map(() => {
        if (agentId) {
          this.refreshForAgent(agentId);
        } else {
          this.refresh();
        }
        return null;
      }),
      catchError((err: HttpErrorResponse) => of(this.toErrorMessage(err)))
    );
  }

  deleteDelivery(delId: string): Observable<string | null> {
    return this.http.delete(`${this.apiUrl}/delivery/${delId}`, { responseType: 'text' }).pipe(
      map(() => {
        this.refresh();
        return null;
      }),
      catchError((err: HttpErrorResponse) => of(this.toErrorMessage(err)))
    );
  }

  private toErrorMessage(err: HttpErrorResponse): string {
    if (err.status === 401 || err.status === 403) {
      return 'Not saved: you must be logged in with an ADMIN account to manage deliveries.';
    }
    return `Not saved: backend error (${err.status || 'network'}). Check the server is running.`;
  }
}
