import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { IProduct } from '../entities/product/product.model';
import { ProductService } from '../entities/product/service/product.service';
import { DatePipe, DecimalPipe, NgClass } from '@angular/common';
import { AuctionCategory } from '../entities/enumerations/auction-category.model';
import { FilterComponent } from './components/filter/filter.component';

@Component({
  templateUrl: 'products.component.html',
  styleUrl: 'products.scss',
  standalone: true,
  imports: [DecimalPipe, DatePipe, NgClass, FilterComponent],
})
export class ProductsComponent implements OnInit {
  authors: any = new Set();
  /**
   *
   * @private
   */
  private readonly productService = inject(ProductService);

  /**
   *
   */
  products = signal<IProduct[]>([]);
  query: any;

  /**
   *
   */
  ngOnInit() {
    this.loadProduct();
    this.getAuthors();
  }

  /**
   *
   */
  loadProduct() {
    this.productService.query(this.query).subscribe(data => {
      if (data.body) this.products.set(data.body);
    });
  }
  getAuthors() {
    this.productService.query({ page: 0 }).subscribe(data => {
      this.authors = data.body?.map(author => {
        if (this.authors.has(!author.authorName)) this.authors.add(author.authorName);
      });
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
  setQuery(event: any) {
    this.query = event;
    this.loadProduct();
  }
}
