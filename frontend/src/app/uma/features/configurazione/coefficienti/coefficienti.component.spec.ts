import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CoefficientiComponent } from './coefficienti.component';

describe('CoefficientiComponent', () => {
  let component: CoefficientiComponent;
  let fixture: ComponentFixture<CoefficientiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CoefficientiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoefficientiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
