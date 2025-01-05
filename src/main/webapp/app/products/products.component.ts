import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { IProduct } from '../entities/product/product.model';
import { ProductService } from '../entities/product/service/product.service';
import { DatePipe, DecimalPipe, NgClass } from '@angular/common';

@Component({
  templateUrl: 'products.component.html',
  styleUrl: 'products.scss',
  standalone: true,
  imports: [DecimalPipe, DatePipe, NgClass],
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
  dropdowns: WritableSignal<{ [key: string]: boolean }> = signal({
    category: false,
  });

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

  /**
   *
   */
  updateDropdown(name: string) {
    this.dropdowns.update(item => {
      item[name] = !item[name];
      return item;
    });
  }
}
