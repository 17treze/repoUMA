import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiLiquidazioneComponent } from './dati-liquidazione.component';

describe('DatiLiquidazioneComponent', () => {
  let component: DatiLiquidazioneComponent;
  let fixture: ComponentFixture<DatiLiquidazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiLiquidazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiLiquidazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
