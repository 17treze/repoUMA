import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovazioneDialogComponent } from './approvazione-dialog.component';

describe('ApprovazioneDialogComponent', () => {
  let component: ApprovazioneDialogComponent;
  let fixture: ComponentFixture<ApprovazioneDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApprovazioneDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovazioneDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
