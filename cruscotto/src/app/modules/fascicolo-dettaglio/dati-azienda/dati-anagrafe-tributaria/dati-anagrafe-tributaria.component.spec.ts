/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DatiAnagrafeTributariaComponent } from './dati-anagrafe-tributaria.component';

describe('DatiAnagrafeTributariaComponent', () => {
  let component: DatiAnagrafeTributariaComponent;
  let fixture: ComponentFixture<DatiAnagrafeTributariaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiAnagrafeTributariaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiAnagrafeTributariaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
