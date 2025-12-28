import { Component, inject, signal, NgZone, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { SunService } from './services/sun.service';
import { AuthService } from './services/auth.service';
import {environment} from '../environments/environment';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  // Injections
  private sunService = inject(SunService);
  private authService = inject(AuthService);
  private http = inject(HttpClient);
  private zone = inject(NgZone);

  // Status-Management mit Signals
  isLoggedIn = signal(false);
  sunData = signal<any>(null);
  locations = signal<any[]>([]); // Liste aus der Datenbank

  // Formular-Variablen
  username = '';
  password = '';
  lat = '48.2082';
  lng = '16.3738';
  date: string = new Date().toISOString().split('T')[0];

  ngOnInit() {
    // Prüfen, ob der User noch eingeloggt ist (Token im LocalStorage)
    const token = this.authService.getToken();
    if (token) {
      this.isLoggedIn.set(true);
      this.fetchLocations(); // Orte direkt laden, wenn Token da ist
    }
  }

  onLogin() {
    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.zone.run(() => {
          this.isLoggedIn.set(true);
          this.fetchLocations(); // Nach erfolgreichem Login die Orte aus der DB holen
        });
      },
      error: () => alert('Login fehlgeschlagen! Bitte prüfe deine Daten.')
    });
  }

  fetchLocations() {
    // Ruft die Liste deiner Entity "Location" vom neuen Java-Controller ab
    this.http.get<any[]>(`${environment.apiUrl}/locations`).subscribe({
      next: (data) => {
        this.zone.run(() => {
          this.locations.set(data);
        });
      },
      error: (err) => console.error('Fehler beim Laden der Orte', err)
    });
  }

  newLocName = '';
  newLocLat = '';
  newLocLng = '';

  addLocation() {
    const newLocation = {
      name: this.newLocName,
      lat: this.newLocLat,
      lng: this.newLocLng
    };

    this.http.post(`${environment.apiUrl}/locations`, newLocation).subscribe({
      next: () => {
        // 1. Felder leeren
        this.newLocName = '';
        this.newLocLat = '';
        this.newLocLng = '';

        // 2. Die Liste der Orte neu vom Server laden (Dropdown aktualisiert sich!)
        this.fetchLocations();

        alert('Ort erfolgreich hinzugefügt!');
      },
      error: (err) => alert('Fehler beim Speichern!')
    });
  }

  deleteLocation(id: number) {
    if (confirm('Möchtest du diesen Ort wirklich löschen?')) {
      this.http.delete(`${environment.apiUrl}/locations/${id}`).subscribe({
        next: () => {
          // Liste neu laden, damit der gelöschte Ort verschwindet
          this.fetchLocations();
        },
        error: (err) => alert('Fehler beim Löschen des Ortes')
      });
    }
  }

  onLocationSelected(event: any) {
    const selectedName = event.target.value;
    // Wir suchen das passende Objekt in unserem Signal
    const loc = this.locations().find(l => l.name === selectedName);

    if (loc) {
      this.lat = loc.lat;
      this.lng = loc.lng;
      // Komfort-Funktion: Lädt die Sonnendaten sofort nach Auswahl
      this.loadData();
    }
  }

  // Funktion, die aufgerufen wird, wenn sich das Datum ändert
  onDateChange() {
    console.log('Neues Datum gewählt:', this.date);
    // Hier kannst du die Funktion aufrufen, die deine Sonnendaten lädt
    this.loadData();
  }

  loadData() {
    this.sunService.getSunInfo(this.lat, this.lng, this.date).subscribe({
      next: (data) => {
        this.zone.run(() => {
          this.sunData.set(data);
        });
      },
      error: (err) => console.error('API Fehler', err)
    });
  }

  onLogout() {
    this.authService.logout();
    this.isLoggedIn.set(false);
    this.sunData.set(null);
    this.locations.set([]);
  }
}
