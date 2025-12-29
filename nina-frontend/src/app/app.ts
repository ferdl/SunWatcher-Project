import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Wichtig für *ngIf

@Component({
  selector: 'app-root',
  standalone: true,           // Markiert die Komponente als Standalone
  imports: [CommonModule],    // Ermöglicht die Nutzung von *ngIf und Lightbox-Logik
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App { // Hier muss 'App' stehen, passend zur Fehlermeldung
  selectedImage: string | null = null;

  openLightbox(imagePath: string) {
    this.selectedImage = imagePath;
    document.body.style.overflow = 'hidden';
  }

  closeLightbox() {
    this.selectedImage = null;
    document.body.style.overflow = 'auto';
  }
}
