/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { PoligoniDichiaratiDialogComponent } from './poligoni-dichiarati-dialog.component';

describe('PoligoniDichiaratiDialogComponent', () => {
  let component: PoligoniDichiaratiDialogComponent;
  let fixture: ComponentFixture<PoligoniDichiaratiDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PoligoniDichiaratiDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PoligoniDichiaratiDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
