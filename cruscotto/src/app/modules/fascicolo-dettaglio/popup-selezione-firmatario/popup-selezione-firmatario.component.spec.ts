/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { PopupSelezioneFirmatarioComponent } from './popup-selezione-firmatario.component';

describe('PopupSelezioneFirmatarioComponent', () => {
  let component: PopupSelezioneFirmatarioComponent;
  let fixture: ComponentFixture<PopupSelezioneFirmatarioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PopupSelezioneFirmatarioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopupSelezioneFirmatarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
