import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScadenzeAppagComponent } from './scadenze-appag.component';

describe('ScadenzeAppagComponent', () => {
  let component: ScadenzeAppagComponent;
  let fixture: ComponentFixture<ScadenzeAppagComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScadenzeAppagComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScadenzeAppagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
