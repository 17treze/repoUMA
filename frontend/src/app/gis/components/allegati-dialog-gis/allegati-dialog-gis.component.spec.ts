/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AllegatiDialogGisComponent } from './allegati-dialog-gis.component';

describe('AllegatiDialogGisComponent', () => {
  let component: AllegatiDialogGisComponent;
  let fixture: ComponentFixture<AllegatiDialogGisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllegatiDialogGisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllegatiDialogGisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
