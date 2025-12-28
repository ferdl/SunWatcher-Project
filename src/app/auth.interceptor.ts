import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';

import { catchError, throwError } from 'rxjs';
import {AuthService} from './services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Explizite Typzuweisung, um TS18046 zu vermeiden
  const authService: AuthService = inject(AuthService);
  const token = authService.getToken();

  let authReq = req;

  // Nur wenn ein Token da ist, fügen wir den Header hinzu
  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // 401: Nicht angemeldet | 403: Keine Rechte
      if (error.status === 401 || error.status === 403) {
        console.warn('Authentifizierungsfehler:', error.status);
        // Hier könnte man den User zum Login umleiten
      }
      return throwError(() => error);
    })
  );
};
