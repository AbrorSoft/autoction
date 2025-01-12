import { Component, output, signal, WritableSignal } from '@angular/core';
import { AuctionCategory } from '../../../entities/enumerations/auction-category.model';
import { NgClass } from '@angular/common';
import { Classification } from '../../../entities/enumerations/classification.model';
import { FormsModule } from '@angular/forms';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap';
import { DATE_TIME_FORMAT } from '../../../config/input.constants';
import dayjs from 'dayjs/esm';

@Component({
  standalone: true,
  templateUrl: 'filter.component.html',
  styleUrl: 'filter.component.scss',
  imports: [NgClass, FormsModule, NgbInputDatepicker],
  selector: 'filter',
})
export class FilterComponent {
  /**
   *
   */
  onFilter = output<any>();

  /**
   *
   */
  dropdowns: WritableSignal<{ [key: string]: boolean }> = signal({
    category: false,
    price: false,
    classification: false,
    date: false,
    author: true,
  });

  auctionCategory = Object.entries(AuctionCategory);
  classifications = Object.entries(Classification);
  selectedClassification: string;
  selectedCategory: string;
  minPrice: number | null;
  maxPrice: number | null;
  auctionDate: any;
  toAuctionDate: any;
  author: any;

  /**
   *
   */
  updateDropdown(name: string) {
    this.dropdowns.update(item => {
      item[name] = !item[name];
      return item;
    });
  }

  /**
   *
   */
  filter() {
    console.log(this.auctionDate);
    this.onFilter.emit(this.query);
  }

  /**
   *
   */
  get query() {
    return {
      'auctionCategory.equals': this.selectedCategory,
      'classification.equals': this.selectedClassification,
      'authorName.contains': this.author ? this.author : '',
      'estimatedPrice.lessThanOrEqual': this.maxPrice ? this.maxPrice : '',
      'estimatedPrice.greaterThanOrEqual': this.minPrice ? this.minPrice : '',
      'producedYear.greaterThanOrEqual': this.auctionDate ? dayjs(this.auctionDate).toISOString() : '',
      'producedYear.lessThanOrEqual': this.toAuctionDate ? dayjs(this.toAuctionDate).toISOString() : '',
    };
  }

  selectCategory(category: string) {
    this.selectedCategory = category;
    this.filter();
  }
  clearFilter() {
    this.selectedCategory = '';
    this.selectedClassification = '';
    this.author = '';
    this.minPrice = null;
    this.maxPrice = null;
    this.auctionDate = undefined;
    this.toAuctionDate = undefined;

    // Optionally close all dropdowns (if desired):
    this.dropdowns.set({
      category: false,
      price: false,
      classification: false,
      date: false,
      author: false,
    });
    this.filter();
  }
  selectClassification(classification: string) {
    this.selectedClassification = classification;
    this.filter();
  }
}
