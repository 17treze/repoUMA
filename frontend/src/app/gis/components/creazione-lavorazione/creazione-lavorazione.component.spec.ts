/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CreazioneLavorazioneComponent } from './creazione-lavorazione.component';

describe('CreazioneLavorazioneComponent', () => {
  let component: CreazioneLavorazioneComponent;
  let fixture: ComponentFixture<CreazioneLavorazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreazioneLavorazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreazioneLavorazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
