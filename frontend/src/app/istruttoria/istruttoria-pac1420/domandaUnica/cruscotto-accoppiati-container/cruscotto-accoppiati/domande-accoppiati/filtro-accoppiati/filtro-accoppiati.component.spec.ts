import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltroAccoppiatiComponent } from './filtro-accoppiati.component';

describe('FiltroACZComponent', () => {
  let component: FiltroAccoppiatiComponent;
  let fixture: ComponentFixture<FiltroAccoppiatiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FiltroAccoppiatiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FiltroAccoppiatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
