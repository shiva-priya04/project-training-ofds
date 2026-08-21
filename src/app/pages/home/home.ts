import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../cart.service';
import { AuthService } from '../../auth.service';
import { MenuItem } from '../menus/menu-data';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  readonly popularItems: MenuItem[] = [];

  constructor(public cart: CartService, public auth: AuthService, private router: Router) {}

  addToCart(item: MenuItem): void {
    this.cart.addItem(item);
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
