import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CartLine, CartService } from '../../cart.service';
import { OrdersService } from '../../orders.service';

@Component({
  selector: 'app-order-confirm',
  imports: [RouterLink],
  templateUrl: './order-confirm.html',
  styleUrl: './order-confirm.css',
})
export class OrderConfirm {
  readonly orderItems: CartLine[];
  readonly subtotal: number;
  readonly deliveryFee: number;
  readonly total: number;
  readonly orderNumber: string;
  readonly estimatedArrival: string;

  constructor(private cart: CartService, private ordersService: OrdersService, private router: Router) {
    this.orderItems = this.cart.items();
    this.subtotal = this.cart.total;
    this.deliveryFee = this.orderItems.length ? 30 : 0;
    this.total = this.subtotal + this.deliveryFee;
    this.orderNumber = 'KK' + Math.floor(100000 + Math.random() * 900000);

    const minutesFromNow = 30 + Math.floor(Math.random() * 16); // 30-45 mins
    const arrival = new Date(Date.now() + minutesFromNow * 60000);
    this.estimatedArrival = arrival.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    const navState = (this.router.getCurrentNavigation()?.extras.state ??
      (typeof history !== 'undefined' ? history.state : undefined)) as
      | { name?: string; address?: string; phone?: string; paymentMethod?: 'cod' | 'upi' | 'card' }
      | undefined;

    this.ordersService.addOrder({
      orderNumber: this.orderNumber,
      restaurant: 'Krusty Krab',
      placedOn: 'Just now',
      trackingStage: 'preparing',
      deliveryFee: this.deliveryFee,
      customerName: navState?.name || 'Guest',
      deliveryAddress: navState?.address || 'Address not provided',
      estimatedArrival: this.estimatedArrival,
      lines: this.orderItems.map((line) => ({ item: line.item, quantity: line.quantity })),
    });

    const restaurantId = this.orderItems.find((line) => line.item.resId)?.item.resId;
    if (restaurantId) {
      const itemIds = [...new Set(this.orderItems.map((line) => line.item.itemId).filter(Boolean))] as string[];
      this.ordersService
        .placeBackendOrder({
          customerName: navState?.name || 'Guest',
          customerPhone: navState?.phone || '',
          customerAddress: navState?.address || '',
          restaurantId,
          itemIds,
          totalAmt: this.total,
          paymentMethod: navState?.paymentMethod || 'cod',
        })
        .subscribe();
    }

    this.cart.clear();
  }

  trackOrder(): void {
    this.router.navigate(['/tracking-order', this.orderNumber]);
  }
}
