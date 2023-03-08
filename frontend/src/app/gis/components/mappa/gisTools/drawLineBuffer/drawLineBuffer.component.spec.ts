/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DrawLineBufferComponent } from './drawLineBuffer.component';

describe('DrawLineBufferComponent', () => {
  let component: DrawLineBufferComponent;
  let fixture: ComponentFixture<DrawLineBufferComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DrawLineBufferComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DrawLineBufferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
