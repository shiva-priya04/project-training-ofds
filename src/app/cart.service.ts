import { Injectable, signal } from '@angular/core';
import { MenuItem } from './pages/menus/menu-data';

export interface CartLine {
  item: MenuItem;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly lines = signal<CartLine[]>([]);
  readonly items = this.lines.asReadonly();

  private readonly toast = signal<string | null>(null);
  readonly toastMessage = this.toast.asReadonly();
  private toastTimeout: ReturnType<typeof setTimeout> | undefined;

  addItem(item: MenuItem): void {
    this.lines.update((lines) => {
      const existing = lines.find((line) => line.item.name === item.name);
      if (existing) {
        return lines.map((line) =>
          line.item.name === item.name ? { ...line, quantity: line.quantity + 1 } : line
        );
      }
      return [...lines, { item, quantity: 1 }];
    });
    this.showToast('Added to cart 🎊');
  }

  private showToast(message: string): void {
    this.toast.set(message);
    if (this.toastTimeout) {
      clearTimeout(this.toastTimeout);
    }
    this.toastTimeout = setTimeout(() => this.toast.set(null), 20000);
  }

  removeItem(item: MenuItem): void {
    this.lines.update((lines) => lines.filter((line) => line.item.name !== item.name));
  }

  increment(item: MenuItem): void {
    this.lines.update((lines) =>
      lines.map((line) =>
        line.item.name === item.name ? { ...line, quantity: line.quantity + 1 } : line
      )
    );
  }

  decrement(item: MenuItem): void {
    this.lines.update((lines) =>
      lines
        .map((line) =>
          line.item.name === item.name ? { ...line, quantity: line.quantity - 1 } : line
        )
        .filter((line) => line.quantity > 0)
    );
  }

  getQuantity(item: MenuItem): number {
    return this.lines().find((line) => line.item.name === item.name)?.quantity ?? 0;
  }

  get total(): number {
    return this.lines().reduce((sum, line) => sum + line.item.price * line.quantity, 0);
  }

  clear(): void {
    this.lines.set([]);
  }
}
