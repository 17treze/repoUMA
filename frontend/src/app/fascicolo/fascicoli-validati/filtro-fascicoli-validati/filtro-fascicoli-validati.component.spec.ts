/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { FiltroFascicoliValidatiComponent } from './filtro-fascicoli-validati.component';

describe('FiltroFascicoliValidatiComponent', () => {
  let component: FiltroFascicoliValidatiComponent;
  let fixture: ComponentFixture<FiltroFascicoliValidatiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FiltroFascicoliValidatiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FiltroFascicoliValidatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
