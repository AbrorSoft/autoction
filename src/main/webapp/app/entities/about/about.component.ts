import { Component } from '@angular/core';
import FooterComponent from '../../layouts/footer/footer.component';

@Component({
  standalone: true,
  templateUrl: 'about.component.html',
  imports: [FooterComponent],
  styleUrl: 'about.component.scss',
})
export class AboutComponent {}
