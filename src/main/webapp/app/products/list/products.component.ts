import { Component, inject, OnInit, signal } from '@angular/core';
import { IProduct } from '../../entities/product/product.model';
import { ProductService } from '../../entities/product/service/product.service';
import { DecimalPipe, JsonPipe, NgClass } from '@angular/common';
import { FilterComponent } from './filter/filter.component';
import { RouterLink } from '@angular/router';
import FormatMediumDatePipe from '../../shared/date/format-medium-date.pipe';

@Component({
  templateUrl: 'products.component.html',
  styleUrl: 'products.scss',
  standalone: true,
  imports: [DecimalPipe, NgClass, FilterComponent, RouterLink, FormatMediumDatePipe, JsonPipe],
})
export class ProductsComponent implements OnInit {
  authors: any[] = [];
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

  /**
   *
   */
  getAuthors() {
    this.productService.query({ page: 0 }).subscribe(data => {
      for (const author of data.body || []) {
        if (!this.authors.includes(author.authorName)) this.authors.push(author.authorName);
      }
    });
  }

  /**
   *
   * @param imageKey
   */
  getImageStable(imageKey?: string | null) {
    return 'api/file/' + imageKey;
  }

  /**
   *
   * @param event
   */
  setQuery(event: any) {
    this.query = event;
    this.loadProduct();
  }
}
