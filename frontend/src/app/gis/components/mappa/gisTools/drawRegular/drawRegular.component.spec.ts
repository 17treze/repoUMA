/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DrawRegularComponent } from './drawRegular.component';

describe('DrawRegularComponent', () => {
  let component: DrawRegularComponent;
  let fixture: ComponentFixture<DrawRegularComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DrawRegularComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DrawRegularComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
