import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: 'restaurant/:id/menu',
    renderMode: RenderMode.Client,
  },
  {
    path: 'tracking-order/:orderNumber',
    renderMode: RenderMode.Client,
  },
  {
    path: '**',
    renderMode: RenderMode.Prerender
  }
];
