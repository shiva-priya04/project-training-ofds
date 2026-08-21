import { Component, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../auth.service';

type AccountRole = 'CUSTOMER' | 'ADMIN' | 'AGENT';

@Component({
  selector: 'app-login',
  imports: [RouterLink, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  loginUsername = '';
  loginPassword = '';
  loginError = signal<string | null>(null);
  loginBusy = signal(false);

  registerUsername = '';
  registerPassword = '';
  confirmPassword = '';
  registerRole: AccountRole = 'CUSTOMER';
  registerError = signal<string | null>(null);
  registerSuccess = signal<string | null>(null);
  registerBusy = signal(false);

  selectedRole: AccountRole = 'CUSTOMER';

  /** Set when arriving via a role-specific button on the home page, e.g. /login?role=ADMIN. */
  readonly context = signal<AccountRole>('CUSTOMER');

  readonly roleLabels: Record<AccountRole, string> = {
    CUSTOMER: 'Customer',
    ADMIN: 'Admin',
    AGENT: 'Delivery Agent',
  };

  constructor(private auth: AuthService, private router: Router, route: ActivatedRoute) {
    const requestedRole = route.snapshot.queryParamMap.get('role');
    const role = this.isValidRole(requestedRole) ? requestedRole : 'CUSTOMER';
    this.selectedRole = role;
    this.context.set(role);
    this.registerRole = role;
  }

  get passwordsMatch(): boolean {
    return this.registerPassword === this.confirmPassword;
  }

  onRoleChange(role: AccountRole): void {
    this.selectedRole = role;
    this.context.set(role);
    this.registerRole = role;
  }

  private isValidRole(role: string | null): role is AccountRole {
    return role === 'CUSTOMER' || role === 'ADMIN' || role === 'AGENT';
  }

  login(): void {
    if (!this.loginUsername || !this.loginPassword) {
      return;
    }
    this.loginError.set(null);
    this.loginBusy.set(true);
    this.auth.login({ username: this.loginUsername, password: this.loginPassword }).subscribe({
      next: () => {
        this.loginBusy.set(false);
        this.redirectByRole();
      },
      error: () => {
        this.loginBusy.set(false);
        this.loginError.set('Invalid username or password.');
      },
    });
  }

  register(): void {
    if (!this.registerUsername || !this.registerPassword || !this.passwordsMatch) {
      return;
    }
    this.registerError.set(null);
    this.registerSuccess.set(null);
    this.registerBusy.set(true);
    this.auth
      .register({ username: this.registerUsername, password: this.registerPassword, role: this.registerRole })
      .subscribe({
        next: () => {
          this.registerBusy.set(false);
          this.registerSuccess.set('Account created. You can now log in.');
          this.registerUsername = '';
          this.registerPassword = '';
          this.confirmPassword = '';
        },
        error: () => {
          this.registerBusy.set(false);
          this.registerError.set('Registration failed. Try a different username.');
        },
      });
  }

  private redirectByRole(): void {
    const role = this.auth.role();
    if (role === 'ADMIN') {
      this.router.navigate(['/admin']);
    } else if (role === 'AGENT') {
      this.router.navigate(['/agent']);
    } else {
      this.router.navigate(['/']);
    }
  }
}
