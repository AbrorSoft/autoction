import { Routes } from '@angular/router';

export default [
  {
    path: 'products',
    loadComponent: () => import('./products.component').then(m => m.ProductsComponent),
  },
] satisfies Routes;
