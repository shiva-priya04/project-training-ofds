import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { RestaurantList } from './pages/restaurant-list/restaurant-list';
import { Menus } from './pages/menus/menus';
import { Cart } from './pages/cart/cart';
import { Checkout } from './pages/checkout/checkout';
import { OrderConfirm } from './pages/order-confirm/order-confirm';
import { MyOrders } from './pages/my-orders/my-orders';
import { TrackingOrder } from './pages/tracking-order/tracking-order';
import { Admin } from './pages/admin/admin';
import { Agent } from './pages/agent/agent';

export const routes: Routes = [
  { path: '', 
    component: Home },
  { path: 'login',
    component: Login },
  { path: 'admin',
    component: Admin },
  { path: 'agent',
    component: Agent },
  { path: 'restaurant-list',
    component: RestaurantList },
  { path: 'menus',
    component: Menus },
  { path: 'restaurant/:id/menu',
    component: Menus },
  { path: 'cart',
    component: Cart },
  { path: 'checkout',
    component: Checkout },
  { path: 'order-confirm',
    component: OrderConfirm },
  { path: 'my-orders',
    component: MyOrders },
  { path: 'tracking-order/:orderNumber',
    component: TrackingOrder },
];
