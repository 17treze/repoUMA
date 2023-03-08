import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadAttachmentsProgressComponent } from './upload-attachments-progress.component';

describe('UploadAttachmentsProgressComponent', () => {
  let component: UploadAttachmentsProgressComponent;
  let fixture: ComponentFixture<UploadAttachmentsProgressComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UploadAttachmentsProgressComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadAttachmentsProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
