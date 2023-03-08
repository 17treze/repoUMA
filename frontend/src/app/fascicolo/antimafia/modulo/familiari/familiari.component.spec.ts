import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FamiliariComponent } from './familiari.component';

describe('FamiliariComponent', () => {
  let component: FamiliariComponent;
  let fixture: ComponentFixture<FamiliariComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FamiliariComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FamiliariComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
