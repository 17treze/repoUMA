/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { SuoloVigenteDaAdlComponent } from './suolo-vigente-da-adl.component';

describe('SuoloVigenteDaAdlComponent', () => {
  let component: SuoloVigenteDaAdlComponent;
  let fixture: ComponentFixture<SuoloVigenteDaAdlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SuoloVigenteDaAdlComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SuoloVigenteDaAdlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
