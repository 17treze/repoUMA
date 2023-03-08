/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EsitoValidazioneLavorazioneComponent } from './esito-validazione-lavorazione.component

describe('EsitoValidazioneLavorazioneComponent', () => {
  let component: EsitoValidazioneLavorazioneComponent;
  let fixture: ComponentFixture<EsitoValidazioneLavorazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EsitoValidazioneLavorazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EsitoValidazioneLavorazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
