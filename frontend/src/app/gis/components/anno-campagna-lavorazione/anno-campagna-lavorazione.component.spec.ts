/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AnnoCampagnaLavorazioneComponent } from './anno-campagna-lavorazione.component';

describe('AnnoCampagnaLavorazioneComponent', () => {
  let component: AnnoCampagnaLavorazioneComponent;
  let fixture: ComponentFixture<AnnoCampagnaLavorazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnnoCampagnaLavorazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnnoCampagnaLavorazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
