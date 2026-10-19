import { HttpClient } from '@angular/common/http';
import { inject, Service, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthResponse } from '../../model/AuthResponse';
import { UserLogin } from '../../model/UserLogin';
import { UserSignup } from '../../model/UserSignup';

@Service()
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/auth';

  private readonly _currentUser = signal<AuthResponse | null>(null);
  readonly currentUser = this._currentUser.asReadonly();

  signup(user: UserSignup): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.baseUrl}/signup`, user)
      .pipe(tap((response) => this._currentUser.set(response)));
  }

  login(user: UserLogin): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.baseUrl}/login`, user,)
      .pipe(tap((response) => this._currentUser.set(response)));
  }

  logout(): void {
    this._currentUser.set(null);
  }
}
