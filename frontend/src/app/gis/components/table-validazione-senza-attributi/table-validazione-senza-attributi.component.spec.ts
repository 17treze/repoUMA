import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableValidazioneSenzaAttributiComponent } from './table-validazione-senza-attributi.component';

describe('TableValidazioneSenzaAttributiComponent', () => {
  let component: TableValidazioneSenzaAttributiComponent;
  let fixture: ComponentFixture<TableValidazioneSenzaAttributiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableValidazioneSenzaAttributiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableValidazioneSenzaAttributiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
