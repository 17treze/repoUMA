import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ControlliSostegnoComponent } from './controlli-sostegno.component';

describe('ControlliSostegnoComponent', () => {
  let component: ControlliSostegnoComponent;
  let fixture: ComponentFixture<ControlliSostegnoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ControlliSostegnoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ControlliSostegnoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
