import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiIstruttoriaComponent } from './dati-istruttoria.component';

describe('DatiIstruttoriaComponent', () => {
  let component: DatiIstruttoriaComponent;
  let fixture: ComponentFixture<DatiIstruttoriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiIstruttoriaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiIstruttoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
