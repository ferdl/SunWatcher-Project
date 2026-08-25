import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface ContactForm {
  name: string;
  email: string;
  phone: string;
  shootingType: string;
  message: string;
}

@Component({
  selector: 'app-kontakt',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './kontakt.html',
  styleUrls: ['./kontakt.css']
})
export class Kontakt {
  formData: ContactForm = {
    name: '',
    email: '',
    phone: '',
    shootingType: 'Newborn',
    message: ''
  };

  isSubmitting = signal(false);
  submitSuccess = signal<boolean | null>(null);

  constructor(private http: HttpClient) {}

  onSubmit() {
    this.isSubmitting.set(true);
    this.submitSuccess.set(null);

    // POST an das Spring-Boot-Backend (/api/contact ist in SecurityConfig freigegeben)
    this.http.post('/api/contact', this.formData, { responseType: 'text' }).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.submitSuccess.set(true);
        this.resetForm();
      },
      error: (err) => {
        console.error('Fehler beim Senden der Anfrage:', err);
        this.isSubmitting.set(false);
        this.submitSuccess.set(false);
      }
    });
  }

  private resetForm() {
    this.formData = {
      name: '',
      email: '',
      phone: '',
      shootingType: 'Newborn',
      message: ''
    };
  }
}
