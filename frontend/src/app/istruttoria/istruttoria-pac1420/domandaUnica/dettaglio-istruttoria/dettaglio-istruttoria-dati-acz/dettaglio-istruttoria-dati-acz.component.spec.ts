import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioIstruttoriaDatiAczComponent } from './dettaglio-istruttoria-dati-acz.component';

describe('DettaglioIstruttoriaDatiAczComponent', () => {
  let component: DettaglioIstruttoriaDatiAczComponent;
  let fixture: ComponentFixture<DettaglioIstruttoriaDatiAczComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioIstruttoriaDatiAczComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioIstruttoriaDatiAczComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
