import { Routes } from '@angular/router';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';

export default [
  {
    path: 'products',
    canActivate: [UserRouteAccessService],
    loadComponent: () => import('./list/products.component').then(m => m.ProductsComponent),
  },
  {
    path: 'products/:id',
    canActivate: [UserRouteAccessService],
    loadComponent: () => import('./detail/product-detail.component').then(m => m.ProductDetailComponent),
  },
] satisfies Routes;
