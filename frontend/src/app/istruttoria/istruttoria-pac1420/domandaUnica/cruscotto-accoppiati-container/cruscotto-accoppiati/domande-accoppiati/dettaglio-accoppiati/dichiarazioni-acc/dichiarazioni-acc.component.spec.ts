/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DichiarazioniAccComponent } from './dichiarazioni-acc.component';

describe('DichiarazioniAccComponent', () => {
  let component: DichiarazioniAccComponent;
  let fixture: ComponentFixture<DichiarazioniAccComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DichiarazioniAccComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DichiarazioniAccComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
