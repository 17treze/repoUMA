import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InformazioniDomandaComponent } from './informazioni-domanda.component';

describe('InformazioniDomandaComponent', () => {
  let component: InformazioniDomandaComponent;
  let fixture: ComponentFixture<InformazioniDomandaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InformazioniDomandaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InformazioniDomandaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
