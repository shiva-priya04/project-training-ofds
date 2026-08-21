import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OrdersService, TrackedOrder, TrackingStage } from '../../orders.service';

type AgentTab = 'profile' | 'assigned' | 'details' | 'status' | 'my-deliveries';

@Component({
  selector: 'app-agent',
  imports: [RouterLink],
  templateUrl: './agent.html',
  styleUrl: './agent.css',
})
export class Agent {
  readonly activeTab = signal<AgentTab>('profile');

  readonly profile = {
    name: 'Ravi Kumar',
    agentId: 'AGT-2291',
    phone: '+91 98450 11223',
    vehicle: 'Two Wheeler - TN 09 AX 4521',
    zone: 'T. Nagar & Anna Salai',
    rating: 4.8,
    joined: 'June 2024',
    avatar: '🛵',
  };

  constructor(private ordersService: OrdersService) {}

  readonly stages: { key: TrackingStage; label: string }[] = [
    { key: 'preparing', label: 'Preparing' },
    { key: 'out-for-delivery', label: 'Out for Delivery' },
    { key: 'delivered', label: 'Delivered' },
    { key: 'cancelled', label: 'Cancelled' },
  ];

  setTab(tab: AgentTab): void {
    this.activeTab.set(tab);
  }

  get myDeliveries(): TrackedOrder[] {
    return this.ordersService.orders().filter((order) => order.assignedAgent === this.profile.name);
  }

  get assignedDeliveries(): TrackedOrder[] {
    return this.myDeliveries.filter(
      (order) => order.trackingStage === 'preparing' || order.trackingStage === 'out-for-delivery'
    );
  }

  get currentDelivery(): TrackedOrder | undefined {
    return (
      this.assignedDeliveries.find((order) => order.trackingStage === 'out-for-delivery') ??
      this.assignedDeliveries[0]
    );
  }

  updateStatus(stage: TrackingStage): void {
    if (!this.currentDelivery) {
      return;
    }
    this.ordersService.updateStage(this.currentDelivery.orderNumber, stage);
  }

  orderTotal(order: TrackedOrder): number {
    const subtotal = order.lines.reduce((sum, line) => sum + line.item.price * line.quantity, 0);
    return subtotal + order.deliveryFee;
  }
}
