/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ToastGisComponent } from './toast-gis.component';

describe('ToastGisComponent', () => {
  let component: ToastGisComponent;
  let fixture: ComponentFixture<ToastGisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ToastGisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ToastGisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
