import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface Payment {
  payId: string;
  orderId: string;
  payMethod: 'UPI' | 'CARD' | 'WALLET' | 'CASH';
  payAmt: number;
  payStatus: 'PENDING' | 'SUCCESS' | 'FAILED' | 'REFUNDED';
}

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  makePayment(payment: Payment): Observable<Payment> {
    return this.http.post<Payment>(`${this.apiUrl}/api/payments`, payment);
  }
}
