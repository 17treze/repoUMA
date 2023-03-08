/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { MisuraComponent } from './misura.component';

describe('MisuraComponent', () => {
  let component: MisuraComponent;
  let fixture: ComponentFixture<MisuraComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MisuraComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MisuraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
