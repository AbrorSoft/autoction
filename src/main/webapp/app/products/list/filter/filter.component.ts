import { Component, output, signal, WritableSignal } from '@angular/core';
import { AuctionCategory } from '../../../entities/enumerations/auction-category.model';
import { NgClass } from '@angular/common';
import { Classification } from '../../../entities/enumerations/classification.model';
import { FormsModule } from '@angular/forms';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap';

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
    date: true,
  });

  auctionCategory = Object.entries(AuctionCategory);
  classifications = Object.entries(Classification);
  selectedClassification: string;
  selectedCategory: string;
  minPrice: number;
  maxPrice: number;
  auctionDate: any;

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
      'estimatedPrice.lessThanOrEqual': this.maxPrice ? this.maxPrice : '',
      'estimatedPrice.greaterThanOrEqual': this.minPrice ? this.minPrice : '',
      'producedYear.equals': this.auctionDate,
    };
  }

  selectCategory(category: string) {
    this.selectedCategory = category;
    this.filter();
  }

  selectClassification(classification: string) {
    this.selectedClassification = classification;
    this.filter();
  }
}
