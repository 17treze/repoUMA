import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioIstruttoriaDatiAcsComponent } from './dettaglio-istruttoria-dati-acs.component';

describe('DettaglioIstruttoriaDatiAcsComponent', () => {
  let component: DettaglioIstruttoriaDatiAcsComponent;
  let fixture: ComponentFixture<DettaglioIstruttoriaDatiAcsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioIstruttoriaDatiAcsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioIstruttoriaDatiAcsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
