import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TslibComponent } from './tslib.component';

describe('TslibComponent', () => {
  let component: TslibComponent;
  let fixture: ComponentFixture<TslibComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TslibComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TslibComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
