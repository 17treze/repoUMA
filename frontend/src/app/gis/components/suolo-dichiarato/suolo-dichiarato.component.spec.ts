/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { SuoloDichiaratoComponent } from './suolo-dichiarato.component';

describe('SuoloDichiaratoComponent', () => {
  let component: SuoloDichiaratoComponent;
  let fixture: ComponentFixture<SuoloDichiaratoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SuoloDichiaratoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SuoloDichiaratoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
