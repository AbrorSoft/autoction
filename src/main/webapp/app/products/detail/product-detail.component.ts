import { Component, inject, OnInit, signal } from '@angular/core';
import { IProduct } from '../../entities/product/product.model';
import { ProductService } from '../../entities/product/service/product.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  templateUrl: 'product-detail.component.html',
  styleUrl: 'product-detail.component.scss',
})
export class ProductDetailComponent implements OnInit {
  /**
   *
   * @private
   * @readonly
   */
  private readonly activatedRoute = inject(ActivatedRoute);
  /**
   *
   * @private
   * @readonly
   */
  private readonly productService = inject(ProductService);

  /**
   *
   */
  product = signal<IProduct | undefined>(undefined);

  /**
   *
   */
  ngOnInit() {
    this.activatedRoute.params.subscribe(data => {
      this.loadProduct(data['id']);
    });
  }

  /**
   *
   * @param id
   */
  loadProduct(id: number) {
    this.productService.find(id).subscribe(data => {
      if (data.body) this.product.set(data.body);
    });
  }

  /**
   *
   * @param imageKey
   */
  getImageStable(imageKey?: BinaryData | null) {
    return 'data:image/png;base64,' + imageKey;
  }
}
