/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ModalCaricaVettorialiComponent } from './modal-carica-vettoriali.component';

describe('ModalCaricaVettorialiComponent', () => {
  let component: ModalCaricaVettorialiComponent;
  let fixture: ComponentFixture<ModalCaricaVettorialiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalCaricaVettorialiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalCaricaVettorialiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
