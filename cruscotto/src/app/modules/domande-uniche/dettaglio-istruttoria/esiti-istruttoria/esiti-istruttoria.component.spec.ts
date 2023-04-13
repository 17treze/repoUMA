import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EsitiIstruttoriaComponent } from './esiti-istruttoria.component';

describe('EsitiIstruttoriaComponent', () => {
  let component: EsitiIstruttoriaComponent;
  let fixture: ComponentFixture<EsitiIstruttoriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EsitiIstruttoriaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EsitiIstruttoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
