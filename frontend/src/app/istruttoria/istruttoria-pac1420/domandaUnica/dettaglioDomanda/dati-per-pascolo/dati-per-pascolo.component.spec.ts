import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiPerPascoloComponent } from './dati-per-pascolo.component';

describe('DatiPerPascoloComponent', () => {
  let component: DatiPerPascoloComponent;
  let fixture: ComponentFixture<DatiPerPascoloComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiPerPascoloComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiPerPascoloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
