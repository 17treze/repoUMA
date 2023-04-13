import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioDomandaPsrStrutturaleComponent } from './dettaglio-domanda-psr-strutturale.component';

describe('DettaglioDomandaComponent', () => {
  let component: DettaglioDomandaPsrStrutturaleComponent;
  let fixture: ComponentFixture<DettaglioDomandaPsrStrutturaleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioDomandaPsrStrutturaleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioDomandaPsrStrutturaleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
