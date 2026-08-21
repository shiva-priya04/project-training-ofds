import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, PLATFORM_ID, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable, catchError, map, of } from 'rxjs';
import { environment } from '../environments/environment';

export interface Agent {
  agentId: string;
  agentName: string;
  agentPhoneNo: string;
}

export interface NewAgent {
  agentName: string;
  agentPhoneNo: string;
}

@Injectable({ providedIn: 'root' })
export class AgentsService {
  private readonly apiUrl = environment.apiUrl;
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));
  private readonly agentsSignal = signal<Agent[]>([]);
  readonly agents = this.agentsSignal.asReadonly();

  constructor(private http: HttpClient) {
    if (this.isBrowser) {
      this.refresh();
    }
  }

  refresh(): void {
    this.http
      .get<Agent[]>(`${this.apiUrl}/agent`)
      .pipe(catchError(() => of([] as Agent[])))
      .subscribe((agents) => this.agentsSignal.set(agents));
  }

  /** Returns null on success, or an error message if the backend rejected the write. */
  addAgent(data: NewAgent): Observable<string | null> {
    const payload = {
      agentId: 'AGT' + Date.now(),
      agentName: data.agentName,
      agentPhoneNo: data.agentPhoneNo,
    };
    return this.http.post<Agent>(`${this.apiUrl}/agent`, payload).pipe(
      map(() => {
        this.refresh();
        return null;
      }),
      catchError((err: HttpErrorResponse) => of(this.toErrorMessage(err)))
    );
  }

  deleteAgent(agentId: string): Observable<string | null> {
    return this.http.delete(`${this.apiUrl}/agent/${agentId}`, { responseType: 'text' }).pipe(
      map(() => {
        this.refresh();
        return null;
      }),
      catchError((err: HttpErrorResponse) => of(this.toErrorMessage(err)))
    );
  }

  private toErrorMessage(err: HttpErrorResponse): string {
    if (err.status === 401 || err.status === 403) {
      return 'Not saved: you must be logged in with an ADMIN account to manage agents.';
    }
    return 'Something went wrong. Please try again.';
  }
}
