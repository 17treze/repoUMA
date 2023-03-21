/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DatiCameraCommercioComponent } from './dati-camera-commercio.component';

describe('DatiCameraCommercioComponent', () => {
  let component: DatiCameraCommercioComponent;
  let fixture: ComponentFixture<DatiCameraCommercioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiCameraCommercioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiCameraCommercioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
