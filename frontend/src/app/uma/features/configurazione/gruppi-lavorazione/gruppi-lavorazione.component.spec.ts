import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GruppiLavorazioneComponent } from './gruppi-lavorazione.component';

describe('GruppiLavorazioneComponent', () => {
  let component: GruppiLavorazioneComponent;
  let fixture: ComponentFixture<GruppiLavorazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GruppiLavorazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GruppiLavorazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
