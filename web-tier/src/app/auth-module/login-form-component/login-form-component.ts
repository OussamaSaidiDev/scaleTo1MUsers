import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { email, form, FormField, FormRoot, required } from '@angular/forms/signals';
import { firstValueFrom } from 'rxjs';
import { UserLogin } from '../../model/UserLogin';
import { AuthService } from '../services/auth-service';

@Component({
  imports: [FormField, FormRoot],
  selector: 'app-login-form-component',
  styleUrl: './login-form-component.css',
  templateUrl: './login-form-component.html',
})
export class LoginFormComponent {
  private readonly authService = inject(AuthService);

  userLogin = signal<UserLogin>({
    email: '',
    password: '',
  });

  loginForm = form(
    this.userLogin,
    (schemaPath) => {
      required(schemaPath.email, { message: 'Email is required' });
      email(schemaPath.email, { message: 'Enter a valid email address' });
      required(schemaPath.password, { message: 'Password is required' });
    },
    {
      submission: {
        action: async () => {
          try {
            await firstValueFrom(this.authService.login(this.userLogin()));
            return ;
          } catch (err) {
            const message =
              err instanceof HttpErrorResponse && err.status === 401
                ? 'Invalid email or password'
                : 'Login failed, please try again';
            return { kind: 'serverError', message };
          }
        },
      },
    },
  );
}
