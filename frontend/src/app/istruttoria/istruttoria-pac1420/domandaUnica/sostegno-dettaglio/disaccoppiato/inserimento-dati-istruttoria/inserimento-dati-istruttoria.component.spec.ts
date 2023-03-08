import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InserimentoDatiIstruttoriaComponent } from './inserimento-dati-istruttoria.component';

describe('InserimentoDatiIstruttoriaComponent', () => {
  let component: InserimentoDatiIstruttoriaComponent;
  let fixture: ComponentFixture<InserimentoDatiIstruttoriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InserimentoDatiIstruttoriaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InserimentoDatiIstruttoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
