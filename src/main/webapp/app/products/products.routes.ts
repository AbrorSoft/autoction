import { Routes } from '@angular/router';

export default [
  {
    path: 'products',
    loadComponent: () => import('./list/products.component').then(m => m.ProductsComponent),
  },
  {
    path: 'products/:id',
    loadComponent: () => import('./detail/product-detail.component').then(m => m.ProductDetailComponent),
  },
] satisfies Routes;
