import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';
import productsRoutes from './products/products.routes';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./layouts/main/main.component').then(m => m.default),
    children: [
      {
        path: '',
        loadComponent: () => import('./home/home.component'),
        title: 'home.title',
      },
      {
        path: '',
        loadComponent: () => import('./layouts/navbar/navbar.component'),
        outlet: 'navbar',
      },
      {
        path: 'admin',
        data: {
          authorities: [Authority.ADMIN],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./admin/admin.routes'),
      },
      {
        path: 'account',
        loadChildren: () => import('./account/account.route'),
      },
      {
        path: 'login',
        loadComponent: () => import('./login/login.component'),
        title: 'login.title',
      },
      {
        path: '',
        loadChildren: () => import(`./entities/entity.routes`),
      },
    ],
  },
  ...productsRoutes,
  ...errorRoute,
];

export default routes;
