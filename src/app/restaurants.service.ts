import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, PLATFORM_ID, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable, catchError, forkJoin, map, of, switchMap, tap } from 'rxjs';
import { environment } from '../environments/environment';
import { AuthService } from './auth.service';
import { MenuItem, RestaurantMenu } from './pages/menus/menu-data';

export interface NewRestaurant {
  name: string;
  area: string;
  icon?: string;
  cuisine?: string;
  type?: 'veg' | 'non-veg';
}

interface BackendRestaurant {
  resId: string;
  resName: string;
  resAddress: string;
  resPhoneNo: string;
  resEmail: string;
  resType?: 'veg' | 'non-veg';
}

interface BackendMenuItem {
  itemId: string;
  itemName: string;
  description: string;
  price: number;
  veg?: boolean;
}

@Injectable({ providedIn: 'root' })
export class RestaurantsService {
  private readonly apiUrl = environment.apiUrl;
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));
  private readonly auth = inject(AuthService);
  private readonly restaurantsSignal = signal<RestaurantMenu[]>([]);
  readonly restaurants = this.restaurantsSignal.asReadonly();

  constructor(private http: HttpClient) {
    if (this.isBrowser) {
      this.refresh();
    }
  }

  refresh(): void {
    this.http
      .get<BackendRestaurant[]>(`${this.apiUrl}/restaurant/all`)
      .pipe(
        switchMap((restaurants) => {
          if (!restaurants.length) {
            return of([] as RestaurantMenu[]);
          }
          const withMenus = restaurants.map((restaurant) =>
            this.http
              .get<BackendMenuItem[]>(`${this.apiUrl}/menu/restaurant/${restaurant.resId}`)
              .pipe(
                catchError(() => of([] as BackendMenuItem[])),
                map((items) => this.toRestaurantMenu(restaurant, items))
              )
          );
          return forkJoin(withMenus);
        }),
        catchError(() => of([] as RestaurantMenu[]))
      )
      .subscribe((restaurants) => this.restaurantsSignal.set(restaurants));
  }

  private toRestaurantMenu(restaurant: BackendRestaurant, items: BackendMenuItem[]): RestaurantMenu {
    return {
      id: restaurant.resId,
      name: restaurant.resName,
      area: restaurant.resAddress,
      icon: '🍽️',
      cuisine: 'Multi-cuisine',
      rating: 4.0,
      time: '30-40 min',
      type: restaurant.resType ?? 'veg',
      items: items.map((item) => ({
        itemId: item.itemId,
        resId: restaurant.resId,
        name: item.itemName,
        description: item.description,
        price: item.price,
        veg: this.toVegFlag(item.veg),
        icon: '🍽️',
      })),
    };
  }

  private toVegFlag(value: unknown): boolean {
    if (value === false || value === 'false' || value === 0 || value === '0' || value === 'non-veg') {
      return false;
    }
    return true;
  }

  /** Returns null on success, or an error message if the backend rejected the write (e.g. missing ADMIN role). */
  addRestaurant(data: NewRestaurant): Observable<string | null> {
    const resId = 'RES' + Date.now();
    const optimisticRestaurant: RestaurantMenu = {
      id: resId,
      name: data.name,
      area: data.area,
      icon: '🍽️',
      cuisine: data.cuisine || 'Multi-cuisine',
      rating: 4.0,
      time: '30-40 min',
      type: data.type ?? 'veg',
      items: [],
    };
    const previousRestaurants = this.restaurantsSignal();
    this.restaurantsSignal.update((restaurants) => [optimisticRestaurant, ...restaurants]);

    const payload = {
      resId,
      resName: data.name,
      resAddress: data.area,
      resPhoneNo: '9000000000',
      resEmail: `${resId.toLowerCase()}@krustykrab.com`,
      resType: data.type ?? 'veg',
    };
    return this.http.post<BackendRestaurant>(`${this.apiUrl}/restaurant/add`, payload, this.authorizedJsonOptions()).pipe(
      map(() => {
        this.refresh();
        return null;
      }),
      catchError((err: HttpErrorResponse) => {
        this.restaurantsSignal.set(previousRestaurants);
        return of(this.toErrorMessage(err));
      })
    );
  }

  deleteRestaurant(id: string): Observable<string | null> {
    return this.http.delete(`${this.apiUrl}/restaurant/delete/${id}`, this.authorizedTextOptions()).pipe(
      map(() => {
        this.refresh();
        return null;
      }),
      catchError((err: HttpErrorResponse) => of(this.toErrorMessage(err)))
    );
  }

  private toErrorMessage(err: HttpErrorResponse): string {
    const backendMessage =
      typeof err.error === 'string'
        ? err.error
        : typeof err.error?.message === 'string'
          ? err.error.message
          : typeof err.error?.error === 'string'
            ? err.error.error
            : null;

    if (err.status === 401 || err.status === 403) {
      return backendMessage ?? 'Not saved: you must be logged in with an ADMIN account to manage restaurants and menus.';
    }
    return backendMessage ?? `Not saved: backend error (${err.status || 'network'}). Check the server is running.`;
  }

  private authorizedJsonOptions() {
    const token = this.auth.token;
    return token ? { headers: { Authorization: `Bearer ${token}` } } : {};
  }

  private authorizedTextOptions() {
    const token = this.auth.token;
    return token
      ? { responseType: 'text' as const, headers: { Authorization: `Bearer ${token}` } }
      : { responseType: 'text' as const };
  }

  getById(id: string): RestaurantMenu | undefined {
    return this.restaurantsSignal().find((restaurant) => restaurant.id === id);
  }

  addMenuItem(restaurantId: string, item: MenuItem): Observable<string | null> {
    const itemId = 'ITEM' + Date.now();
    const optimisticItem: MenuItem = {
      ...item,
      itemId,
      resId: restaurantId,
      veg: this.toVegFlag(item.veg),
      icon: item.icon ?? '🍽️',
    };
    const previousRestaurants = this.restaurantsSignal();
    this.restaurantsSignal.update((restaurants) =>
      restaurants.map((restaurant) =>
        restaurant.id !== restaurantId
          ? restaurant
          : { ...restaurant, items: [...restaurant.items, optimisticItem] }
      )
    );

    const payload = {
      itemId,
      itemName: item.name,
      description: item.description,
      price: item.price,
      veg: this.toVegFlag(item.veg),
      restaurant: { resId: restaurantId },
    };
    console.debug('[RestaurantsService] addMenuItem payload:', payload);
    return this.http.post(`${this.apiUrl}/menu/add`, payload, this.authorizedJsonOptions()).pipe(
      tap((res) => console.debug('[RestaurantsService] addMenuItem response:', res)),
      map(() => {
        this.refresh();
        return null;
      }),
      catchError((err: HttpErrorResponse) => {
        console.error('[RestaurantsService] addMenuItem error:', err);
        this.restaurantsSignal.set(previousRestaurants);
        return of(this.toErrorMessage(err));
      })
    );
  }

  deleteMenuItem(restaurantId: string, itemId: string): Observable<string | null> {
    const hasItem = this
      .getById(restaurantId)
      ?.items.some((menuItem) => menuItem.itemId === itemId);

    if (!itemId || !hasItem) {
      return of('Not saved: this item was never synced with the backend.');
    }

    return this.http.delete(`${this.apiUrl}/menu/delete/${itemId}`, this.authorizedTextOptions()).pipe(
      map(() => {
        this.refresh();
        return null;
      }),
      catchError((err: HttpErrorResponse) => of(this.toErrorMessage(err)))
    );
  }
}

