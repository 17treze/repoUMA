import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PopupEredeComponent } from './popup-erede.component';

describe('PopupEredeComponent', () => {
  let component: PopupEredeComponent;
  let fixture: ComponentFixture<PopupEredeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PopupEredeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopupEredeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
