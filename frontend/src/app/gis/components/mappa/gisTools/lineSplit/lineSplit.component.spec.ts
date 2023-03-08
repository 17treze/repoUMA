/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { LineSplitComponent } from './lineSplit.component';

describe('LineSplitComponent', () => {
  let component: LineSplitComponent;
  let fixture: ComponentFixture<LineSplitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LineSplitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LineSplitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
