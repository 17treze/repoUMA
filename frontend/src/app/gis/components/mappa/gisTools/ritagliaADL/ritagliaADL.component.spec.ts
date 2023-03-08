/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { RitagliaADLComponent } from './ritagliaADL.component';

describe('RitagliaADLComponent', () => {
  let component: RitagliaADLComponent;
  let fixture: ComponentFixture<RitagliaADLComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RitagliaADLComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RitagliaADLComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
