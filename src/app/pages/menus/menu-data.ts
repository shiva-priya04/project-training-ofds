export interface MenuItem {
  itemId?: string;
  resId?: string;
  name: string;
  description: string;
  price: number;
  veg: boolean;
  icon: string;
}

export interface RestaurantMenu {
  id: string;
  name: string;
  area: string;
  items: MenuItem[];
  icon?: string;
  cuisine?: string;
  rating?: number;
  time?: string;
  type?: 'veg' | 'non-veg';
}

export const MENUS: RestaurantMenu[] = [];

