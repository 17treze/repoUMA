import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RicercaFascicoliComponent } from './ricerca-fascicoli.component';

describe('RicercaFascicoliComponent', () => {
  let component: RicercaFascicoliComponent;
  let fixture: ComponentFixture<RicercaFascicoliComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RicercaFascicoliComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RicercaFascicoliComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
