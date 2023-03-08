import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiSospensioneComponent } from './dati-sospensione.component';

describe('DatiSospensioneComponent', () => {
  let component: DatiSospensioneComponent;
  let fixture: ComponentFixture<DatiSospensioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiSospensioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiSospensioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
