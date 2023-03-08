import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScadenzeComponent } from './scadenze.component';

describe('ScadenzeComponent', () => {
  let component: ScadenzeComponent;
  let fixture: ComponentFixture<ScadenzeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScadenzeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScadenzeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
