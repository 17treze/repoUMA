/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DragPanComponent } from './dragPan.component';

describe('DragPanComponent', () => {
  let component: DragPanComponent;
  let fixture: ComponentFixture<DragPanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DragPanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DragPanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
