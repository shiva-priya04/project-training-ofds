import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestaurantsService, NewRestaurant } from './restaurants.service';
import { OrdersService, TrackedOrder } from './orders.service';
import { AgentsService, NewAgent, Agent } from './agents.service';
import { DeliveryService, NewDelivery, Delivery } from './delivery.service';
import { MenuItem, RestaurantMenu } from './pages/menus/menu-data';

export type AdminTab = 'profile' | 'restaurants' | 'orders' | 'delivery' | 'agents';

/**
 * Facade service that centralizes all admin-page business logic (restaurants,
 * menu items, agents and delivery assignment) so the Admin component only
 * has to deal with view state (active tab, form models, error messages).
 */
@Injectable({ providedIn: 'root' })
export class AdminService {
  constructor(
    private restaurantsService: RestaurantsService,
    private ordersService: OrdersService,
    private agentsService: AgentsService,
    private deliveryService: DeliveryService
  ) {}

  /** Refresh only the data relevant to the tab being shown. */
  refreshForTab(tab: AdminTab): void {
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
      this.agentsService.refresh();
      this.deliveryService.refresh();
    }
  }

  // ----- Restaurants & menu items -----

  get restaurants(): RestaurantMenu[] {
    return this.restaurantsService.restaurants();
  }

  getRestaurantById(id: string): RestaurantMenu | undefined {
    return this.restaurantsService.getById(id);
  }

  addRestaurant(data: NewRestaurant): Observable<string | null> {
    return this.restaurantsService.addRestaurant(data);
  }

  deleteRestaurant(id: string): Observable<string | null> {
    return this.restaurantsService.deleteRestaurant(id);
  }

  addMenuItem(restaurantId: string, item: MenuItem): Observable<string | null> {
    return this.restaurantsService.addMenuItem(restaurantId, item);
  }

  deleteMenuItem(restaurantId: string, itemId: string): Observable<string | null> {
    return this.restaurantsService.deleteMenuItem(restaurantId, itemId);
  }

  // ----- Orders -----

  get orders(): TrackedOrder[] {
    return this.ordersService.orders();
  }

  get activeDeliveries(): TrackedOrder[] {
    return this.orders.filter((order) => order.trackingStage === 'out-for-delivery');
  }

  // ----- Agents -----

  get agents(): Agent[] {
    return this.agentsService.agents();
  }

  addAgent(data: NewAgent): Observable<string | null> {
    return this.agentsService.addAgent(data);
  }

  deleteAgent(agentId: string): Observable<string | null> {
    return this.agentsService.deleteAgent(agentId);
  }

  // ----- Delivery assignment -----

  get deliveries(): Delivery[] {
    return this.deliveryService.deliveries();
  }

  get unassignedOrders(): TrackedOrder[] {
    const assignedOrderIds = new Set(this.deliveries.map((delivery) => delivery.order.orderId));
    return this.orders.filter((order) => !assignedOrderIds.has(order.orderNumber));
  }

  assignDelivery(data: NewDelivery): Observable<string | null> {
    return this.deliveryService.assignDelivery(data);
  }

  deleteDelivery(delId: string): Observable<string | null> {
    return this.deliveryService.deleteDelivery(delId);
  }
}
