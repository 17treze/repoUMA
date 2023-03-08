import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MotivazioneRifiutoDialogComponent } from './motivazione-rifiuto-dialog.component';

describe('MotivazioneRifiutoDialogComponent', () => {
  let component: MotivazioneRifiutoDialogComponent;
  let fixture: ComponentFixture<MotivazioneRifiutoDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MotivazioneRifiutoDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MotivazioneRifiutoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
