import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiesteInLavorazioneComponent } from './richieste-in-lavorazione.component';

describe('RichiesteInLavorazioneComponent', () => {
  let component: RichiesteInLavorazioneComponent;
  let fixture: ComponentFixture<RichiesteInLavorazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiesteInLavorazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiesteInLavorazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
