import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CtrlSostegnoAccComponent } from './ctrl-sostegno-acc.component';

describe('CtrlSostegnoAcc', () => {
  let component: CtrlSostegnoAccComponent;
  let fixture: ComponentFixture<CtrlSostegnoAccComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CtrlSostegnoAccComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CtrlSostegnoAccComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
