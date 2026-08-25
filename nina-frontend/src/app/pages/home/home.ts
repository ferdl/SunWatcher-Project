import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home implements OnInit {
  randomImages = signal<string[]>([]);
  selectedImage = signal<string | null>(null);

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<string[]>('/api/gallery/images').subscribe({
      next: (allImages) => {
        // Mische alle Bilder aus allen Unterordnern zufällig durch
        const shuffled = [...allImages].sort(() => 0.5 - Math.random());

        // Nimm nur die ersten 4 Bilder für genau eine Zeile
        const previewSelection = shuffled.slice(0, 4);

        this.randomImages.set(previewSelection);
      }
    });
  }

  openLightbox(img: string) { this.selectedImage.set(img); }
  closeLightbox() { this.selectedImage.set(null); }
}
