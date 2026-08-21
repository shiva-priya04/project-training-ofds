import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { OrdersService, TrackedOrder, TrackingStage } from '../../orders.service';

export type OrderStatus = 'placed' | 'delivered' | 'cancelled';

@Component({
  selector: 'app-my-orders',
  imports: [RouterLink],
  templateUrl: './my-orders.html',
  styleUrl: './my-orders.css',
})
export class MyOrders {
  constructor(private ordersService: OrdersService, private router: Router) {}

  get orders(): TrackedOrder[] {
    return this.ordersService.orders();
  }

  subtotal(order: TrackedOrder): number {
    return order.lines.reduce((sum, line) => sum + line.item.price * line.quantity, 0);
  }

  total(order: TrackedOrder): number {
    return this.subtotal(order) + (order.trackingStage === 'cancelled' ? 0 : order.deliveryFee);
  }

  isTrackable(order: TrackedOrder): boolean {
    return order.trackingStage === 'preparing' || order.trackingStage === 'out-for-delivery';
  }

  trackOrder(order: TrackedOrder): void {
    this.router.navigate(['/tracking-order', order.orderNumber]);
  }

  status(stage: TrackingStage): OrderStatus {
    if (stage === 'delivered') return 'delivered';
    if (stage === 'cancelled') return 'cancelled';
    return 'placed';
  }

  statusLabel(stage: TrackingStage): string {
    switch (this.status(stage)) {
      case 'placed':
        return 'Order Placed';
      case 'delivered':
        return 'Delivered';
      case 'cancelled':
        return 'Cancelled';
    }
  }
}

