/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { MessaggiGisComponent } from './messaggi-gis.component';

describe('MessaggiGisComponent', () => {
  let component: MessaggiGisComponent;
  let fixture: ComponentFixture<MessaggiGisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessaggiGisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessaggiGisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
