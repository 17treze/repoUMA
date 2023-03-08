import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigurazioneIstruttoriaComponent } from './configurazione-istruttoria.component';

describe('ConfigurazioneIstruttoriaComponent', () => {
  let component: ConfigurazioneIstruttoriaComponent;
  let fixture: ComponentFixture<ConfigurazioneIstruttoriaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigurazioneIstruttoriaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurazioneIstruttoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
