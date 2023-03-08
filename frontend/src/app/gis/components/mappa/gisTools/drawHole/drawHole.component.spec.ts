/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DrawHoleComponent } from './drawHole.component';

describe('DrawHoleComponent', () => {
  let component: DrawHoleComponent;
  let fixture: ComponentFixture<DrawHoleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DrawHoleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DrawHoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
