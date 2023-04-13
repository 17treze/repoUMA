import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioIstruttoriaPsrStrutturaleComponent } from './dettaglio-istruttoria-psr-strutturale.component';

describe('DettaglioDomandaComponent', () => {
  let component: DettaglioIstruttoriaPsrStrutturaleComponent;
  let fixture: ComponentFixture<DettaglioIstruttoriaPsrStrutturaleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioIstruttoriaPsrStrutturaleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioIstruttoriaPsrStrutturaleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
