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
  restaurantMenu: RestaurantMenu | undefined;
  allMenus: RestaurantMenu[] = [];

  constructor(route: ActivatedRoute, public cart: CartService, restaurantsService: RestaurantsService) {
    const idParam = route.snapshot.paramMap.get('id');
    if (idParam !== null) {
      this.restaurantMenu = restaurantsService.getById(idParam);
    } else {
      this.allMenus = restaurantsService.restaurants();
    }
  }

  addToCart(item: MenuItem): void {
    this.cart.addItem({ ...item, resId: item.resId ?? this.restaurantMenu?.id });
  }
}
