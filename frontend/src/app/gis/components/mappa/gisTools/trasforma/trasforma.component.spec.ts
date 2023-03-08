/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { TrasformaComponent } from './trasforma.component';

describe('TrasformaComponent', () => {
  let component: TrasformaComponent;
  let fixture: ComponentFixture<TrasformaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TrasformaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrasformaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
