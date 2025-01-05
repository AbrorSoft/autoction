import { Component, signal, WritableSignal } from '@angular/core';
import { AuctionCategory } from '../../../entities/enumerations/auction-category.model';
import { NgClass } from '@angular/common';
import { Classification } from '../../../entities/enumerations/classification.model';

@Component({
  standalone: true,
  templateUrl: 'filter.component.html',
  styleUrl: 'filter.component.scss',
  imports: [NgClass],
  selector: 'filter',
})
export class FilterComponent {
  /**
   *
   */
  dropdowns: WritableSignal<{ [key: string]: boolean }> = signal({
    category: false,
    price: true,
    classification: false,
  });

  auctionCategory = Object.entries(AuctionCategory);
  classifications = Object.entries(Classification);
  selectedClassification: string;
  selectedCategory: string;

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
