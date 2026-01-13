import { Component, OnInit, signal, HostListener } from '@angular/core'; // signal hinzufügen
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import { HttpEventType, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule], // CommonModule ist wichtig für *ngIf/*ngFor
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App implements OnInit {
  // Signal für den Fortschritt (0 bis 100 oder null)
  uploadProgress = signal<number | null>(null);
  // Signal für die lokale Bildvorschau
  imagePreview = signal<string | null>(null);
  selectedFile: File | null = null; // Um die Datei zwischenzuspeichern

  // Neue Signals für den Toast
  toastMessage = signal<string | null>(null);
  toastType = signal<'success' | 'error'>('success');

  // Wir definieren images als Signal
  images = signal<string[]>([]);
  isAdmin = signal<boolean>(false);

  selectedImage: string | null = null;
  private readonly imageBaseUrl = '/api/images-serve/';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      try {
        // Wir dekodieren den Token (Payload), um das Ablaufdatum zu prüfen
        const payload = JSON.parse(atob(token.split('.')[1]));
        const expiry = payload.exp * 1000; // exp ist in Sekunden

        if (Date.now() < expiry) {
          this.isAdmin.set(true);
          this.loadImages();
        } else {
          // Token abgelaufen!
          this.logout();
        }
      } catch (e) {
        this.logout(); // Fehler beim Dekodieren -> weg damit
      }
    }
  }

  loadImages() {
    this.http.get<string[]>('/api/gallery/images').subscribe({
      next: (filenames) => {
        const mappedUrls = filenames.map(name => `${this.imageBaseUrl}${name}`);
        // Ein Signal wird über .set() aktualisiert
        this.images.set(mappedUrls);
      },
      error: (err) => console.error('Fehler beim Laden:', err)
    });
  }

  // Objekt für das Kontaktformular
  contact = {
    name: '',
    email: '',
    message: ''
  };

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
        next: (_response) => {
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
  @HostListener('window:keydown', ['$event'])
  handleKeyDown(event: KeyboardEvent) {
    if (event.ctrlKey && event.shiftKey && event.key === 'L') {
      const password = prompt("Admin-Passwort eingeben:");

      if (password) {
        // Wir senden das Passwort an deinen Auth-Endpunkt
        this.http.post<{token: string}>('/api/auth/login', {
          username: 'admin', // Oder dein Benutzername
          password: password
        }).subscribe({
          next: (response) => {
            // HIER platzierst du den Befehl:
            localStorage.setItem('jwt_token', response.token);

            this.isAdmin.set(true);
            alert('Login erfolgreich! Admin-Modus aktiviert.');
          },
          error: (err) => {
            console.error('Login fehlgeschlagen', err);
            alert('Falsches Passwort oder Serverfehler.');
          }
        });
      }
    }
  }

  // nina-frontend/src/app/app.ts

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (file) {
      const formData = new FormData();
      if (file) {
        this.selectedFile = file;

        // FileReader initialisieren, um das Bild lokal zu lesen
        const reader = new FileReader();
        reader.onload = () => {
          this.imagePreview.set(reader.result as string);
        };
        reader.readAsDataURL(file);
      }
      formData.append("file", file);

      // 1. Hol dir das Token aus dem lokalen Speicher
      const token = localStorage.getItem('jwt_token');

      // 2. Sende das Token im 'Authorization' Header mit
      this.http.post('/api/gallery/upload', formData, {
        headers: { 'Authorization': `Bearer ${token}` }, // Das ist der fehlende Schlüssel!
        responseType: 'text'
      }).subscribe({
        next: (_response) => {
          this.showToast('Bild erfolgreich hochgeladen!'); // Statt alert
          this.loadImages();
        },
        error: () => this.showToast('Upload fehlgeschlagen!', 'error')
      });
    }
  }
  deleteImage(imageUrl: string) {
    if (!confirm('Wirklich löschen?')) return;

    // 1. Dateiname extrahieren (z.B. "bild.jpg")
    const filename = imageUrl.split('/').pop();

    // 2. Token holen
    const token = localStorage.getItem('jwt_token');

    // 3. Den Request absenden - ACHTE AUF DEN SLASH vor ${filename}
    this.http.delete(`/api/gallery/images/${filename}`, {
      headers: { 'Authorization': `Bearer ${token}` },
      responseType: 'text'
    }).subscribe({
      next: () => {
        this.images.update(prev => prev.filter(img => img !== imageUrl));
        this.showToast('Bild wurde gelöscht.');
      },
      error: () => this.showToast('Löschen nicht möglich.', 'error')
    });
  }
  // Hilfsmethode zum Anzeigen des Toasts
  showToast(message: string, type: 'success' | 'error' = 'success') {
    this.toastMessage.set(message);
    this.toastType.set(type);

    // Nach 3 Sekunden automatisch ausblenden
    setTimeout(() => {
      this.toastMessage.set(null);
    }, 3000);
  }

  uploadImage() {
    if (!this.selectedFile) return;

    const formData = new FormData();
    formData.append('file', this.selectedFile);

    const token = localStorage.getItem('jwt_token');

    this.http.post('/api/gallery/upload', formData, {
      headers: new HttpHeaders({ 'Authorization': `Bearer ${token}` }),
      reportProgress: true,   // WICHTIG: Fortschritt melden
      observe: 'events',      // WICHTIG: Auf alle Events hören (nicht nur auf die Antwort)
      responseType: 'text'
    }).subscribe({
      next: (event: any) => {
        if (event.type === HttpEventType.UploadProgress) {
          // Prozent berechnen
          const percentDone = Math.round(100 * event.loaded / (event.total || 1));
          this.uploadProgress.set(percentDone);
        } else if (event.type === HttpEventType.Response) {
          // Upload fertig
          this.showToast('Bild erfolgreich hochgeladen!');
          this.uploadProgress.set(null); // Balken ausblenden
          this.cancelPreview();
          this.loadImages();
        }
      },
      error: () => {
        this.showToast('Upload fehlgeschlagen!', 'error');
        this.uploadProgress.set(null);
      }
    });
  }

  cancelPreview() {
    this.imagePreview.set(null);
    this.selectedFile = null;
  }

  logout() {
    localStorage.removeItem('jwt_token'); // Das "Dauerticket" löschen
    this.isAdmin.set(false);              // UI umschalten
    this.imagePreview.set(null);          // Vorschau aufräumen
    this.showToast('Erfolgreich abgemeldet');
  }
}
