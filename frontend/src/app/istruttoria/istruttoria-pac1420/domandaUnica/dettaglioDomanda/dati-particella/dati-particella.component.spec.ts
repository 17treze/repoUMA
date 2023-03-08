import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiParticellaComponent } from './dati-particella.component';

describe('DatiParticellaComponent', () => {
  let component: DatiParticellaComponent;
  let fixture: ComponentFixture<DatiParticellaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiParticellaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiParticellaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
