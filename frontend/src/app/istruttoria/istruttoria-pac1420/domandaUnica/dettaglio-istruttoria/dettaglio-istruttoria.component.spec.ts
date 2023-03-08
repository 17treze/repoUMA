import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioIstruttoriaComponent } from './dettaglio-istruttoria.component';

describe('DettaglioIstruttoriaComponent', () => {
  let component: DettaglioIstruttoriaComponent;
  let fixture: ComponentFixture<DettaglioIstruttoriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioIstruttoriaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioIstruttoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
