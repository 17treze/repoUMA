import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InIstruttoriaDialogComponent } from './in-istruttoria-dialog.component';

describe('InIstruttoriaDialogComponent', () => {
  let component: InIstruttoriaDialogComponent;
  let fixture: ComponentFixture<InIstruttoriaDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InIstruttoriaDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InIstruttoriaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
