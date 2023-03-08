import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiParticellaAccComponent } from './dati-particella-acc.component';

describe('DatiParticellaAccComponent', () => {
  let component: DatiParticellaAccComponent;
  let fixture: ComponentFixture<DatiParticellaAccComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiParticellaAccComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiParticellaAccComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
