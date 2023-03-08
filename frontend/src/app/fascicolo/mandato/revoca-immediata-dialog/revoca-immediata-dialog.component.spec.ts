import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RevocaImmediataDialogComponent } from './revoca-immediata-dialog.component';

describe('ApprovazioneDialogComponent', () => {
  let component: RevocaImmediataDialogComponent;
  let fixture: ComponentFixture<RevocaImmediataDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RevocaImmediataDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RevocaImmediataDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
