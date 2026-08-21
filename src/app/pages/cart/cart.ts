import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartService } from '../../cart.service';

@Component({
  selector: 'app-cart',
  imports: [RouterLink],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart {
  constructor(public cart: CartService) {}

  get deliveryFee(): number {
    return this.cart.items().length ? 30 : 0;
  }

  get total(): number {
    return this.cart.total + this.deliveryFee;
  }
}
