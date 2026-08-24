import { Component, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AgentsService } from '../../agents.service';
import { AuthService } from '../../auth.service';
import { Delivery, DeliveryService, DeliveryStatus } from '../../delivery.service';

type AgentTab = 'profile' | 'assigned' | 'details' | 'status' | 'my-deliveries';

@Component({
  selector: 'app-agent',
  imports: [RouterLink],
  templateUrl: './agent.html',
  styleUrl: './agent.css',
})
export class Agent {
  readonly activeTab = signal<AgentTab>('profile');
  readonly actionError = signal<string | null>(null);

  readonly profile = computed(() => {
    const username = this.auth.username();
    const matchedAgent = this.agentsService.agents().find((agent) => agent.agentName === username);
    return {
      name: matchedAgent?.agentName ?? username ?? 'Agent',
      agentId: matchedAgent?.agentId ?? 'Not assigned yet',
      phone: matchedAgent?.agentPhoneNo ?? 'Not provided',
      vehicle: 'Not provided',
      zone: 'Not assigned',
      rating: 0,
      joined: 'Recently',
      avatar: '🛵',
    };
  });

  constructor(
    private agentsService: AgentsService,
    private deliveryService: DeliveryService,
    private auth: AuthService
  ) {
    this.agentsService.refresh();
    this.deliveryService.refresh();
  }

  setTab(tab: AgentTab): void {
    this.activeTab.set(tab);
    this.actionError.set(null);
  }

  /** All deliveries currently assigned to this logged-in agent. */
  get myDeliveries(): Delivery[] {
    const agentId = this.profile().agentId;
    return this.deliveryService.deliveries().filter((delivery) => delivery.agent?.agentId === agentId);
  }

  /** Newly assigned deliveries awaiting an accept/decline decision. */
  get pendingDeliveries(): Delivery[] {
    return this.myDeliveries.filter((delivery) => delivery.delStatus === 'ASSIGNED');
  }

  /** The delivery the agent is currently working on (accepted, en route, etc). */
  get currentDelivery(): Delivery | undefined {
    return this.myDeliveries.find(
      (delivery) => delivery.delStatus === 'ACCEPTED' || delivery.delStatus === 'OUT_FOR_DELIVERY'
    );
  }

  acceptDelivery(delId: string): void {
    this.setStatus(delId, 'ACCEPTED');
  }

  declineDelivery(delId: string): void {
    this.setStatus(delId, 'DECLINED');
  }

  markOutForDelivery(delId: string): void {
    this.setStatus(delId, 'OUT_FOR_DELIVERY');
  }

  markDelivered(delId: string): void {
    this.setStatus(delId, 'DELIVERED');
  }

  private setStatus(delId: string, status: DeliveryStatus): void {
    this.actionError.set(null);
    const agentId = this.profile().agentId;
    this.deliveryService.updateStatus(delId, status, agentId).subscribe((error) => {
      if (error) {
        this.actionError.set(error);
      }
    });
  }

  statusLabel(status: DeliveryStatus): string {
    switch (status) {
      case 'ASSIGNED':
        return 'Awaiting Response';
      case 'ACCEPTED':
        return 'Accepted';
      case 'DECLINED':
        return 'Declined';
      case 'OUT_FOR_DELIVERY':
        return 'Out for Delivery';
      case 'DELIVERED':
        return 'Delivered';
    }
  }

  statusClass(status: DeliveryStatus): string {
    return 'status-badge--' + status.toLowerCase().replace(/_/g, '-');
  }

  orderTotal(delivery: Delivery): number {
    return delivery.order.totalAmt ?? 0;
  }

  restaurantName(delivery: Delivery): string {
    return delivery.order.restaurant?.resName ?? delivery.order.restaurant?.resId ?? 'Unknown';
  }

  customerName(delivery: Delivery): string {
    return delivery.order.customer?.customerName ?? delivery.order.customer?.customerId ?? 'Unknown';
  }

  customerPhone(delivery: Delivery): string {
    return delivery.order.customer?.customerPhoneNo ?? 'N/A';
  }

  customerAddress(delivery: Delivery): string {
    return delivery.order.customer?.customerAddress ?? 'Not provided';
  }
}
