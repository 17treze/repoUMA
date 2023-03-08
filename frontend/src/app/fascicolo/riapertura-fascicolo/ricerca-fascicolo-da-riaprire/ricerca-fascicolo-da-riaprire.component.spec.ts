import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RicercaFascicoloDaRiaprireComponent } from './ricerca-fascicolo-da-riaprire.component';

describe('RicercaFascicoloDaRiaprireComponent', () => {
  let component: RicercaFascicoloDaRiaprireComponent;
  let fixture: ComponentFixture<RicercaFascicoloDaRiaprireComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RicercaFascicoloDaRiaprireComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RicercaFascicoloDaRiaprireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
