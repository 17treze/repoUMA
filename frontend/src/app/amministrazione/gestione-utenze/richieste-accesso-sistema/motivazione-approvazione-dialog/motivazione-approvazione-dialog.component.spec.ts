import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MotivazioneApprovazioneDialogComponent } from './motivazione-approvazione-dialog.component';

describe('MotivazioneApprovazioneDialogComponent', () => {
  let component: MotivazioneApprovazioneDialogComponent;
  let fixture: ComponentFixture<MotivazioneApprovazioneDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MotivazioneApprovazioneDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MotivazioneApprovazioneDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
