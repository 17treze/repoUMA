/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DrawTraceComponent } from './drawTrace.component';

describe('DrawTraceComponent', () => {
  let component: DrawTraceComponent;
  let fixture: ComponentFixture<DrawTraceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DrawTraceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DrawTraceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
