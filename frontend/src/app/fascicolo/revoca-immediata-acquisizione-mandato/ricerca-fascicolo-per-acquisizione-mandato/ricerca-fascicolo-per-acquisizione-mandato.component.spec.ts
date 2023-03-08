import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RicercaFascicoloPerAcquisizioneMandatoComponent } from './ricerca-fascicolo-per-acquisizione-mandato.component';

describe('RevocaImmediataRicercaFascicoloDaRiaprireComponent', () => {
  let component: RicercaFascicoloPerAcquisizioneMandatoComponent;
  let fixture: ComponentFixture<RicercaFascicoloPerAcquisizioneMandatoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RicercaFascicoloPerAcquisizioneMandatoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RicercaFascicoloPerAcquisizioneMandatoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
