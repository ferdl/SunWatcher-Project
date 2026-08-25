import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-familie',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './familie.html',
  styleUrls: ['./familie.css']
})
export class Familie implements OnInit {
  images = signal<string[]>([]);
  selectedImage = signal<string | null>(null);

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<string[]>('/api/gallery/images').subscribe({
      next: (allImages) => {
        const categoryImages = allImages.filter(img => img.toLowerCase().includes('familie'));
        this.images.set(categoryImages);
      }
    });
  }

  openLightbox(img: string) { this.selectedImage.set(img); }
  closeLightbox() { this.selectedImage.set(null); }
}
