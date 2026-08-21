import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackingOrder } from './tracking-order';

describe('TrackingOrder', () => {
  let component: TrackingOrder;
  let fixture: ComponentFixture<TrackingOrder>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackingOrder],
    }).compileComponents();

    fixture = TestBed.createComponent(TrackingOrder);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
