import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CruscottoSostegnoComponent } from './cruscotto-sostegno.component';

describe('CruscottoSostegnoComponent', () => {
  let component: CruscottoSostegnoComponent;
  let fixture: ComponentFixture<CruscottoSostegnoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CruscottoSostegnoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CruscottoSostegnoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
