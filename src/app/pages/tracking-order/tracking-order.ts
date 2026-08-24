import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { OrdersService, TrackedOrder } from '../../orders.service';

interface TrackingStep {
  key: 'preparing' | 'out-for-delivery' | 'delivered';
  label: string;
  icon: string;
}

@Component({
  selector: 'app-tracking-order',
  imports: [RouterLink],
  templateUrl: './tracking-order.html',
  styleUrl: './tracking-order.css',
})
export class TrackingOrder implements OnInit, OnDestroy {
  private readonly orderNumber: string;
  private pollHandle?: ReturnType<typeof setInterval>;

  readonly steps: TrackingStep[] = [
    { key: 'preparing', label: 'Preparing your meal', icon: '👨‍🍳' },
    { key: 'out-for-delivery', label: 'Out for delivery', icon: '🛵' },
    { key: 'delivered', label: 'Delivered successfully', icon: '✅' },
  ];

  constructor(route: ActivatedRoute, private ordersService: OrdersService) {
    this.orderNumber = route.snapshot.paramMap.get('orderNumber') ?? '';
  }

  get order(): TrackedOrder | undefined {
    return this.ordersService.getByNumber(this.orderNumber);
  }

  ngOnInit(): void {
    this.ordersService.refresh();
    // Poll the backend so the delivery status agents set stays in sync here
    // without requiring the customer to manually reload the page.
    this.pollHandle = setInterval(() => this.ordersService.refresh(), 8000);
  }

  ngOnDestroy(): void {
    if (this.pollHandle) {
      clearInterval(this.pollHandle);
    }
  }

  subtotal(): number {
    if (!this.order) return 0;
    return this.order.lines.reduce((sum, line) => sum + line.item.price * line.quantity, 0);
  }

  total(): number {
    if (!this.order) return 0;
    return this.subtotal() + (this.order.trackingStage === 'cancelled' ? 0 : this.order.deliveryFee);
  }

  currentStepIndex(): number {
    if (!this.order) return -1;
    return this.steps.findIndex((step) => step.key === this.order!.trackingStage);
  }

  isStepDone(index: number): boolean {
    return index <= this.currentStepIndex();
  }
}
