/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { PoligoniDichiaratiTableComponent } from './poligoni-dichiarati-table.component';

describe('PoligoniDichiaratiTableComponent', () => {
  let component: PoligoniDichiaratiTableComponent;
  let fixture: ComponentFixture<PoligoniDichiaratiTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PoligoniDichiaratiTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PoligoniDichiaratiTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
