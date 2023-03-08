/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CaricaLayerStoricoComponent } from './carica-layer-storico.component';

describe('CaricaLayerStoricoComponent', () => {
  let component: CaricaLayerStoricoComponent;
  let fixture: ComponentFixture<CaricaLayerStoricoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaricaLayerStoricoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaricaLayerStoricoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
