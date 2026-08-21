import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RestaurantsService } from '../../restaurants.service';
import { OrdersService } from '../../orders.service';
import { AgentsService, NewAgent } from '../../agents.service';
import { MenuItem, RestaurantMenu } from '../menus/menu-data';

type AdminTab = 'profile' | 'restaurants' | 'orders' | 'delivery' | 'agents';

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

  readonly profile = {
    name: 'Priya Raman',
    email: 'priya.raman@krustykrab.com',
    role: 'Super Admin',
    phone: '+91 98765 43210',
    joined: 'March 2023',
    avatar: '🧑‍💼',
  };

  selectedRestaurantId: string | null = null;
  actionError = signal<string | null>(null);

  newRestaurant: NewRestaurantForm = { name: '', area: '', cuisine: '', type: 'veg' };
  newItem: NewMenuItemForm = { name: '', description: '', price: 0, veg: true };

  newAgent: NewAgent = { agentName: '', agentPhoneNo: '' };
  agentActionError = signal<string | null>(null);

  constructor(
    private restaurantsService: RestaurantsService,
    private ordersService: OrdersService,
    private agentsService: AgentsService
  ) {}

  setTab(tab: AdminTab): void {
    this.activeTab.set(tab);
    if (tab !== 'restaurants') {
      this.selectedRestaurantId = null;
    }
    // Targeted refresh: fetch fresh data only when the admin views that tab
    if (tab === 'restaurants') {
      this.restaurantsService.refresh();
    }
    if (tab === 'orders') {
      this.ordersService.refresh();
    }
    if (tab === 'agents') {
      this.agentsService.refresh();
    }
    if (tab === 'delivery') {
      this.ordersService.refresh();
    }
  }

  get restaurants(): RestaurantMenu[] {
    return this.restaurantsService.restaurants();
  }

  get totalRestaurants(): number {
    return this.restaurants.length;
  }

  get selectedRestaurant(): RestaurantMenu | undefined {
    return this.selectedRestaurantId !== null
      ? this.restaurantsService.getById(this.selectedRestaurantId)
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
    this.restaurantsService.addRestaurant({ ...this.newRestaurant }).subscribe((error) => {
      if (error) {
        this.actionError.set(error);
        return;
      }
      this.newRestaurant = { name: '', area: '', cuisine: '', type: 'veg' };
    });
  }

  deleteRestaurant(id: string): void {
    this.actionError.set(null);
    this.restaurantsService.deleteRestaurant(id).subscribe((error) => {
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
    this.restaurantsService.addMenuItem(this.selectedRestaurantId, item).subscribe((error) => {
      if (error) {
        this.actionError.set(error);
        return;
      }
      this.newItem = { name: '', description: '', price: 0, veg: true };
    });
  }

  deleteMenuItem(itemName: string): void {
    if (this.selectedRestaurantId === null) {
      return;
    }
    this.actionError.set(null);
    this.restaurantsService.deleteMenuItem(this.selectedRestaurantId, itemName).subscribe((error) => {
      if (error) {
        this.actionError.set(error);
      }
    });
  }

  get orders() {
    return this.ordersService.orders();
  }

  get activeDeliveries() {
    return this.orders.filter((order) => order.trackingStage === 'out-for-delivery');
  }

  get agents() {
    return this.agentsService.agents();
  }

  get totalAgents(): number {
    return this.agents.length;
  }

  addAgent(): void {
    if (!this.newAgent.agentName.trim() || !/^\d{10}$/.test(this.newAgent.agentPhoneNo)) {
      return;
    }
    this.agentActionError.set(null);
    this.agentsService.addAgent({ ...this.newAgent }).subscribe((error) => {
      if (error) {
        this.agentActionError.set(error);
        return;
      }
      this.newAgent = { agentName: '', agentPhoneNo: '' };
    });
  }

  deleteAgent(agentId: string): void {
    this.agentActionError.set(null);
    this.agentsService.deleteAgent(agentId).subscribe((error) => {
      if (error) {
        this.agentActionError.set(error);
      }
    });
  }
}
