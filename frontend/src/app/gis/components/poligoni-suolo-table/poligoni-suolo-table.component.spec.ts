/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { PoligoniSuoloTableComponent } from './poligoni-suolo-table.component';

describe('PoligoniSuoloTableComponent', () => {
  let component: PoligoniSuoloTableComponent;
  let fixture: ComponentFixture<PoligoniSuoloTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PoligoniSuoloTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PoligoniSuoloTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
