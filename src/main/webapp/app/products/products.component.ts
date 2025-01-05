import { Component } from '@angular/core';
import { IProduct } from '../entities/product/product.model';

@Component({
  templateUrl: 'products.component.html',
  standalone: true,
})
export class ProductsComponent {
  products: IProduct[] = [];
}
