/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { MappaComponent } from './mappa.component';

describe('MappaComponent', () => {
  let component: MappaComponent;
  let fixture: ComponentFixture<MappaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MappaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MappaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
