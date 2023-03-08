/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CaricaVettorialiComponent } from './carica-vettoriali.component';

describe('CaricaVettorialiComponent', () => {
  let component: CaricaVettorialiComponent;
  let fixture: ComponentFixture<CaricaVettorialiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaricaVettorialiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaricaVettorialiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
