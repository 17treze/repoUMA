/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { UndoRedoComponent } from './undoRedo.component';

describe('UndoRedoComponent', () => {
  let component: UndoRedoComponent;
  let fixture: ComponentFixture<UndoRedoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UndoRedoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UndoRedoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
