import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PopupRunMigrazioneComponent } from './popup-run-migrazione.component';

describe('PopupRunMigrazioneComponent', () => {
  let component: PopupRunMigrazioneComponent;
  let fixture: ComponentFixture<PopupRunMigrazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PopupRunMigrazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopupRunMigrazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
