/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CloseHoleComponent } from './closeHole.component';

describe('CloseHoleComponent', () => {
  let component: CloseHoleComponent;
  let fixture: ComponentFixture<CloseHoleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CloseHoleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CloseHoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
