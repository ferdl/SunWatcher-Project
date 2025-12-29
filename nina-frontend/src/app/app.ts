import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  // Variable für die Lightbox
  selectedImage: string | null = null;

  // Objekt für das Kontaktformular
  contact = {
    name: '',
    email: '',
    message: ''
  };

  constructor(private http: HttpClient) {}

  // --- Lightbox Methoden ---
  openLightbox(imagePath: string) {
    this.selectedImage = imagePath;
    document.body.style.overflow = 'hidden'; // Scrollen verhindern
  }

  closeLightbox() {
    this.selectedImage = null;
    document.body.style.overflow = 'auto'; // Scrollen wieder erlauben
  }

  // --- Kontakt-Logik ---
  onSubmit() {
    // Hier wird der echte POST-Request an dein Java-Backend gesendet
    this.http.post('/api/contact', this.contact, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          alert(`Vielen Dank, ${this.contact.name}! Deine Nachricht wurde erfolgreich versendet.`);
          // Formular nach Erfolg leeren
          this.contact = { name: '', email: '', message: '' };
        },
        error: (err) => {
          console.error('Fehler beim Senden:', err);
          alert('Ups! Deine Nachricht konnte nicht gesendet werden. Bitte prüfe, ob das Backend läuft.');
        }
      });
  }
}
