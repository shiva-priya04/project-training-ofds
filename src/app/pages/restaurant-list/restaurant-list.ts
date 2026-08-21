import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MenuItem, RestaurantMenu } from '../menus/menu-data';
import { CartService } from '../../cart.service';
import { RestaurantsService } from '../../restaurants.service';

interface Restaurant {
  id: string;
  name: string;
  cuisine: string;
  area: string;
  rating: number;
  time: string;
  type: 'veg' | 'non-veg';
  icon: string;
}

@Component({
  selector: 'app-restaurant-list',
  imports: [RouterLink],
  templateUrl: './restaurant-list.html',
  styleUrl: './restaurant-list.css',
})
export class RestaurantList {
  constructor(public cart: CartService, private restaurantsService: RestaurantsService) {}

  get restaurants(): Restaurant[] {
    return this.restaurantsService.restaurants().map((restaurant) => ({
      id: restaurant.id,
      name: restaurant.name,
      cuisine: restaurant.cuisine ?? 'Multi-cuisine',
      area: restaurant.area,
      rating: restaurant.rating ?? 4.0,
      time: restaurant.time ?? '30-40 min',
      type: restaurant.type ?? (restaurant.items.every((item) => item.veg) ? 'veg' : 'non-veg'),
      icon: restaurant.icon ?? '🍽️',
    }));
  }

  get vegRestaurants() {
    return this.restaurants.filter((r) => r.type === 'veg');
  }

  get nonVegRestaurants() {
    return this.restaurants.filter((r) => r.type === 'non-veg');
  }

  selectedMenu: RestaurantMenu | undefined;

  openMenu(id: string): void {
    this.selectedMenu = this.restaurantsService.getById(id);
  }

  closeMenu(): void {
    this.selectedMenu = undefined;
  }

  addToCart(item: MenuItem): void {
    this.cart.addItem({ ...item, resId: this.selectedMenu?.id });
  }
}
