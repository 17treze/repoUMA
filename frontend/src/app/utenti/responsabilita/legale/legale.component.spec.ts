import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LegaleComponent } from './legale.component';

describe('LegaleComponent', () => {
  let component: LegaleComponent;
  let fixture: ComponentFixture<LegaleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LegaleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LegaleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
