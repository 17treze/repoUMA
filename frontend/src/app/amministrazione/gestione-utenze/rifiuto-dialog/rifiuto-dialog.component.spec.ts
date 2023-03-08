import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RifiutoDialogComponent } from './rifiuto-dialog.component';

describe('RifiutoDialogComponent', () => {
  let component: RifiutoDialogComponent;
  let fixture: ComponentFixture<RifiutoDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RifiutoDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RifiutoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
