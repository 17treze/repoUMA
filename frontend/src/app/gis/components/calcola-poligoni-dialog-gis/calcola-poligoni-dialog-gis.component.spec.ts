/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CalcolaPoligoniDialogGisComponent } from './calcola-poligoni-dialog-gis.component';

describe('CalcolaPoligoniDialogGisComponent', () => {
  let component: CalcolaPoligoniDialogGisComponent;
  let fixture: ComponentFixture<CalcolaPoligoniDialogGisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CalcolaPoligoniDialogGisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CalcolaPoligoniDialogGisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
