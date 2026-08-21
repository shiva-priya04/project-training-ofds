import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Home } from './home';

describe('Home', () => {
  let component: Home;
  let fixture: ComponentFixture<Home>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Home],
    }).compileComponents();

    fixture = TestBed.createComponent(Home);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show a single login/register entry point', () => {
    fixture.detectChanges();
    const loginLinks = Array.from(fixture.nativeElement.querySelectorAll('a')) as HTMLAnchorElement[];
    const loginLink = loginLinks.find((el) => el.getAttribute('href')?.includes('/login'));

    expect(loginLinks.filter((el) => el.getAttribute('href')?.includes('/login'))).toHaveLength(1);
    expect(loginLink).toBeTruthy();
    expect(loginLink?.textContent).toContain('Login / Register');
    expect(loginLink?.getAttribute('href')).not.toContain('role=');
  });
});
