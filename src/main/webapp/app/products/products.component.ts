import { Component, inject, OnInit, signal } from '@angular/core';
import { IProduct } from '../entities/product/product.model';
import { ProductService } from '../entities/product/service/product.service';

@Component({
  templateUrl: 'products.component.html',
  standalone: true,
})
export class ProductsComponent implements OnInit {
  /**
   *
   * @private
   */
  private readonly productService = inject(ProductService);

  /**
   *
   */
  products = signal<IProduct[]>([]);

  /**
   *
   */
  ngOnInit() {
    this.loadProduct();
  }

  /**
   *
   */
  loadProduct() {
    this.productService.query().subscribe(data => {
      if (data.body) this.products.set(data.body);
    });
  }

  /**
   *
   */
  getImageStable(imageKey?: BinaryData | null) {
    return 'data:image/png;base64,' + imageKey;
  }
}
