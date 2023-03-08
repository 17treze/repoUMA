/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { FascicoliValidatiComponent } from './fascicoli-validati.component';

describe('FascicoliValidatiComponent', () => {
  let component: FascicoliValidatiComponent;
  let fixture: ComponentFixture<FascicoliValidatiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FascicoliValidatiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FascicoliValidatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
