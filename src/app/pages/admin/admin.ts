import { Component, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService, AdminTab } from '../../admin.service';
import { NewAgent } from '../../agents.service';
import { NewDelivery } from '../../delivery.service';
import { AuthService } from '../../auth.service';
import { MenuItem, RestaurantMenu } from '../menus/menu-data';

interface NewRestaurantForm {
  name: string;
  area: string;
  cuisine: string;
  type: 'veg' | 'non-veg';
}

interface NewMenuItemForm {
  name: string;
  description: string;
  price: number;
  veg: boolean;
}

@Component({
  selector: 'app-admin',
  imports: [RouterLink, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin {
  readonly activeTab = signal<AdminTab>('profile');

  readonly profile = computed(() => {
    const username = this.auth.username();
    const role = this.auth.role();
    return {
      name: username ?? 'Admin',
      email: username ? `${username}@ofds-admin.local` : 'Not provided',
      role: role === 'ADMIN' ? 'Super Admin' : role ?? 'Admin',
      phone: 'Not provided',
      joined: 'Not provided',
      avatar: '🧑‍💼',
    };
  });

  selectedRestaurantId: string | null = null;
  actionError = signal<string | null>(null);

  newRestaurant: NewRestaurantForm = { name: '', area: '', cuisine: '', type: 'veg' };
  newItem: NewMenuItemForm = { name: '', description: '', price: 0, veg: true };

  newAgent: NewAgent = { agentName: '', agentPhoneNo: '' };
  agentActionError = signal<string | null>(null);

  newDelivery: NewDelivery = { orderId: '', agentId: '', estimatedTimeOfArrival: '' };
  deliveryActionError = signal<string | null>(null);

  constructor(private adminService: AdminService, private auth: AuthService) {}

  setTab(tab: AdminTab): void {
    this.activeTab.set(tab);
    if (tab !== 'restaurants') {
      this.selectedRestaurantId = null;
    }
    // Targeted refresh: fetch fresh data only when the admin views that tab
    this.adminService.refreshForTab(tab);
  }

  get restaurants(): RestaurantMenu[] {
    return this.adminService.restaurants;
  }

  get totalRestaurants(): number {
    return this.restaurants.length;
  }

  get selectedRestaurant(): RestaurantMenu | undefined {
    return this.selectedRestaurantId !== null
      ? this.adminService.getRestaurantById(this.selectedRestaurantId)
      : undefined;
  }

  manageMenu(id: string): void {
    this.selectedRestaurantId = id;
  }

  backToRestaurants(): void {
    this.selectedRestaurantId = null;
  }

  addRestaurant(): void {
    if (!this.newRestaurant.name.trim() || !this.newRestaurant.area.trim()) {
      return;
    }
    this.actionError.set(null);
    this.adminService.addRestaurant({ ...this.newRestaurant }).subscribe((error) => {
      if (error) {
        this.actionError.set(error);
        return;
      }
      this.newRestaurant = { name: '', area: '', cuisine: '', type: 'veg' };
    });
  }

  deleteRestaurant(id: string): void {
    this.actionError.set(null);
    this.adminService.deleteRestaurant(id).subscribe((error) => {
      if (error) {
        this.actionError.set(error);
        return;
      }
      if (this.selectedRestaurantId === id) {
        this.selectedRestaurantId = null;
      }
    });
  }

  addMenuItem(): void {
    if (this.selectedRestaurantId === null || !this.newItem.name.trim() || this.newItem.price <= 0) {
      return;
    }
    this.actionError.set(null);
    const item: MenuItem = { ...this.newItem, icon: '🍽️' };
    this.adminService.addMenuItem(this.selectedRestaurantId, item).subscribe((error) => {
      if (error) {
        this.actionError.set(error);
        return;
      }
      this.newItem = { name: '', description: '', price: 0, veg: true };
    });
  }

  deleteMenuItem(itemId?: string): void {
    if (this.selectedRestaurantId === null) {
      return;
    }
    if (!itemId) {
      this.actionError.set('Not saved: this item has no backend ID yet. Please refresh and try again.');
      return;
    }
    this.actionError.set(null);
    this.adminService.deleteMenuItem(this.selectedRestaurantId, itemId).subscribe((error) => {
      if (error) {
        this.actionError.set(error);
      }
    });
  }

  get orders() {
    return this.adminService.orders;
  }

  get activeDeliveries() {
    return this.adminService.activeDeliveries;
  }

  get agents() {
    return this.adminService.agents;
  }

  get totalAgents(): number {
    return this.agents.length;
  }

  addAgent(): void {
    if (!this.newAgent.agentName.trim() || !/^\d{10}$/.test(this.newAgent.agentPhoneNo)) {
      return;
    }
    this.agentActionError.set(null);
    this.adminService.addAgent({ ...this.newAgent }).subscribe((error) => {
      if (error) {
        this.agentActionError.set(error);
        return;
      }
      this.newAgent = { agentName: '', agentPhoneNo: '' };
    });
  }

  deleteAgent(agentId: string): void {
    this.agentActionError.set(null);
    this.adminService.deleteAgent(agentId).subscribe((error) => {
      if (error) {
        this.agentActionError.set(error);
      }
    });
  }

  get deliveries() {
    return this.adminService.deliveries;
  }

  get unassignedOrders() {
    return this.adminService.unassignedOrders;
  }

  assignDelivery(): void {
    if (!this.newDelivery.orderId || !this.newDelivery.agentId || !this.newDelivery.estimatedTimeOfArrival) {
      return;
    }
    this.deliveryActionError.set(null);
    this.adminService.assignDelivery({ ...this.newDelivery }).subscribe((error) => {
      if (error) {
        this.deliveryActionError.set(error);
        return;
      }
      this.newDelivery = { orderId: '', agentId: '', estimatedTimeOfArrival: '' };
    });
  }

  deleteDelivery(delId: string): void {
    this.deliveryActionError.set(null);
    this.adminService.deleteDelivery(delId).subscribe((error) => {
      if (error) {
        this.deliveryActionError.set(error);
      }
    });
  }
}

