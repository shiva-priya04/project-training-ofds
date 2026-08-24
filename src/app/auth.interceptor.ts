import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const token = auth.token;

  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // 401 = not authenticated (missing/invalid/expired token) -> session is
      // no longer valid, force the user back to login.
      // 403 = authenticated but not permitted for this specific action (e.g.
      // logged in as CUSTOMER trying an ADMIN-only endpoint) -> the session
      // itself is still fine, so let the error bubble up and show an inline
      // message instead of kicking the user out.
      if (error.status === 401) {
        auth.logout();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
