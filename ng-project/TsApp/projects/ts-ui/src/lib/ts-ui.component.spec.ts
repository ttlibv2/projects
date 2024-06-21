import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TsUiComponent } from './ts-ui.component';

describe('TsUiComponent', () => {
  let component: TsUiComponent;
  let fixture: ComponentFixture<TsUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TsUiComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TsUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
