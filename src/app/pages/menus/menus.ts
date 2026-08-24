import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MenuItem, RestaurantMenu } from './menu-data';
import { CartService } from '../../cart.service';
import { RestaurantsService } from '../../restaurants.service';

@Component({
  selector: 'app-menus',
  imports: [RouterLink],
  templateUrl: './menus.html',
  styleUrl: './menus.css',
})
export class Menus {
  private readonly restaurantId: string | null;

  constructor(route: ActivatedRoute, public cart: CartService, private restaurantsService: RestaurantsService) {
    this.restaurantId = route.snapshot.paramMap.get('id');
  }

  get restaurantMenu(): RestaurantMenu | undefined {
    return this.restaurantId !== null ? this.restaurantsService.getById(this.restaurantId) : undefined;
  }

  get allMenus(): RestaurantMenu[] {
    return this.restaurantId === null ? this.restaurantsService.restaurants() : [];
  }

  addToCart(item: MenuItem): void {
    this.cart.addItem({ ...item, resId: item.resId ?? this.restaurantMenu?.id });
  }
}
