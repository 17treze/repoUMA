/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { SpegniVettorialiComponent } from './spegni-vettoriali.component';

describe('SpegniVettorialiComponent', () => {
  let component: SpegniVettorialiComponent;
  let fixture: ComponentFixture<SpegniVettorialiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpegniVettorialiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpegniVettorialiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
