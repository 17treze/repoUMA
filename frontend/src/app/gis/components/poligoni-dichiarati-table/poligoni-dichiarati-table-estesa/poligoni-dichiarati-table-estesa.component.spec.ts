/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { PoligoniDichiaratiTableEstesaComponent } from './poligoni-dichiarati-table-estesa.component';

describe('PoligoniDichiaratiTableEstesaComponent', () => {
  let component: PoligoniDichiaratiTableEstesaComponent;
  let fixture: ComponentFixture<PoligoniDichiaratiTableEstesaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PoligoniDichiaratiTableEstesaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PoligoniDichiaratiTableEstesaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
