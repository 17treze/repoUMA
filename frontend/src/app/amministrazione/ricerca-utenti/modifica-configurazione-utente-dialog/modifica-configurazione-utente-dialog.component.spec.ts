import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificaConfigurazioneUtenteDialogComponent } from './modifica-configurazione-utente-dialog.component';

describe('ModificaConfigurazioneUtenteDialogComponent', () => {
  let component: ModificaConfigurazioneUtenteDialogComponent;
  let fixture: ComponentFixture<ModificaConfigurazioneUtenteDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModificaConfigurazioneUtenteDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModificaConfigurazioneUtenteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
