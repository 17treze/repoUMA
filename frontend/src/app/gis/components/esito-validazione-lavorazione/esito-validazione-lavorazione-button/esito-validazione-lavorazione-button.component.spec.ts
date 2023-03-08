/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EsitoValidazioneLavorazioneButtonComponent } from './esito-lavorazione-button.component';
describe('EsitoValidazioneLavorazioneButtonComponent', () => {
  let component: EsitoValidazioneLavorazioneButtonComponent;
  let fixture: ComponentFixture<EsitoValidazioneLavorazioneButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EsitoValidazioneLavorazioneButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EsitoValidazioneLavorazioneButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
