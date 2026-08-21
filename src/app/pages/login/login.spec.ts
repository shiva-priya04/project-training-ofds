import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Login } from './login';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Login],
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should let the user choose a sign-in role inside the login screen', () => {
    fixture.detectChanges();
    const roleSelect = fixture.nativeElement.querySelector('#loginRole');

    expect(roleSelect).not.toBeNull();
    expect(roleSelect.value).toBe('CUSTOMER');
    expect(fixture.nativeElement.textContent).toContain('Choose who you want to sign in as');
  });
});
