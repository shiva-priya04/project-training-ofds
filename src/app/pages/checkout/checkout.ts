import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../cart.service';

@Component({
  selector: 'app-checkout',
  imports: [FormsModule, RouterLink],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css',
})
export class Checkout {
  name = '';
  address = '';
  phoneNumber = '';
  paymentMethod = 'cod';
  upiId = '';
  cardNumber = '';
  cardExpiry = '';
  cardCvv = '';

  constructor(public cart: CartService, private router: Router) {}

  get deliveryFee(): number {
    return this.cart.items().length ? 30 : 0;
  }

  get total(): number {
    return this.cart.total + this.deliveryFee;
  }

  placeOrder(): void {
    this.router.navigate(['/order-confirm'], {
      state: {
        name: this.name,
        address: this.address,
        phone: this.phoneNumber,
        paymentMethod: this.paymentMethod,
      },
    });
  }
}
