/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AllegatiGisComponent } from './allegati-gis.component';

describe('AllegatiGisComponent', () => {
  let component: AllegatiGisComponent;
  let fixture: ComponentFixture<AllegatiGisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllegatiGisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllegatiGisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
