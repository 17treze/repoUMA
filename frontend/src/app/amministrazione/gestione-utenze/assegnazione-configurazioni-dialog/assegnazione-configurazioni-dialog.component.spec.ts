import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssegnazioneConfigurazioniDialogComponent } from './assegnazione-configurazioni-dialog.component';

describe('VisualizzazioneConfigurazioniDialogComponent', () => {
  let component: AssegnazioneConfigurazioniDialogComponent;
  let fixture: ComponentFixture<AssegnazioneConfigurazioniDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssegnazioneConfigurazioniDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssegnazioneConfigurazioniDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
