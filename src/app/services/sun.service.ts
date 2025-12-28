import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SunData } from '../sun-data.model';

@Injectable({ providedIn: 'root' })
export class SunService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) { }

  getSunInfo(lat: string, lng: string, date: string): Observable<SunData> {
    const url = `${this.apiUrl}/sun?lat=${lat}&lng=${lng}&date=${date}`;

    // Einfach nur der Request – der Interceptor fügt den Header automatisch an!
    return this.http.get<SunData>(url);
  }
}
