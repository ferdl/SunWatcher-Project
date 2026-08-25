import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-babybauch',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './babybauch.html',
  styleUrls: ['./babybauch.css']
})
export class Babybauch implements OnInit {
  images = signal<string[]>([]);
  selectedImage = signal<string | null>(null);

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // Lädt alle Galeriebilder und filtert diejenigen aus dem Ordner /babybauch/
    this.http.get<string[]>('/api/gallery/images').subscribe({
      next: (allImages) => {
        console.log('Alle Bilder vom Backend:', allImages); //
        // Sucht unabhängig von Groß-/Kleinschreibung nach "babybauch" im Pfad
        const categoryImages = allImages.filter(img =>
          img.toLowerCase().includes('babybauch')
        );
        console.log('Gefilterte Babybauch-Bilder:', categoryImages); // <-- HIER LOGGEN
        this.images.set(categoryImages);
      }
    });
  }

  openLightbox(img: string) { this.selectedImage.set(img); }
  closeLightbox() { this.selectedImage.set(null); }
}
