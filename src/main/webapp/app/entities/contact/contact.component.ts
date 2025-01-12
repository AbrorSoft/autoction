import { Component } from '@angular/core';
import FooterComponent from '../../layouts/footer/footer.component';

@Component({
  standalone: true,
  templateUrl: 'contact.component.html',
  imports: [FooterComponent],
  styleUrl: 'contact.component.scss',
})
export class ContactComponent {}
