import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'auctionApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'auctionApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'about',
    data: { pageTitle: 'about' },
    loadChildren: () => import('./about/about.routes'),
  },
  {
    path: 'contact',
    data: { pageTitle: 'contact' },
    loadChildren: () => import('./contact/contact.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
