import { HttpClient } from '@angular/common/http';
import { Injectable, PLATFORM_ID, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable, tap } from 'rxjs';
import { environment } from '../environments/environment';

export interface RegisterRequest {
  username: string;
  password: string;
  role: 'CUSTOMER' | 'ADMIN' | 'AGENT';
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  role: string;
}

const TOKEN_KEY = 'ofds_token';
const USERNAME_KEY = 'ofds_username';
const ROLE_KEY = 'ofds_role';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = environment.apiUrl;
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));

  private readonly usernameSignal = signal<string | null>(this.readStorage(USERNAME_KEY));
  private readonly roleSignal = signal<string | null>(this.normalizeRole(this.readStorage(ROLE_KEY)));

  readonly username = this.usernameSignal.asReadonly();
  readonly role = this.roleSignal.asReadonly();

  constructor(private http: HttpClient) {}

  private readStorage(key: string): string | null {
    return this.isBrowser ? localStorage.getItem(key) : null;
  }

  private normalizeRole(role: string | null): string | null {
    if (!role) {
      return null;
    }
    const normalized = role.trim().replace(/^ROLE_/i, '').toUpperCase();
    return normalized || null;
  }

  get token(): string | null {
    return this.readStorage(TOKEN_KEY);
  }

  get isLoggedIn(): boolean {
    return !!this.token;
  }

  register(request: RegisterRequest): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/auth/register`,
      request,
      {
        responseType: 'text',
        headers: { 'Content-Type': 'application/json' }
      }
    );
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/auth/login`,
      request,
      { headers: { 'Content-Type': 'application/json' } }
    ).pipe(
      tap((response) => {
        const role = this.normalizeRole(response.role) ?? 'CUSTOMER';
        if (!this.isBrowser) {
          return;
        }
        localStorage.setItem(TOKEN_KEY, response.token);
        localStorage.setItem(USERNAME_KEY, response.username);
        localStorage.setItem(ROLE_KEY, role);
        this.usernameSignal.set(response.username);
        this.roleSignal.set(role);
      })
    );
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USERNAME_KEY);
      localStorage.removeItem(ROLE_KEY);
    }
    this.usernameSignal.set(null);
    this.roleSignal.set(null);
  }
}
