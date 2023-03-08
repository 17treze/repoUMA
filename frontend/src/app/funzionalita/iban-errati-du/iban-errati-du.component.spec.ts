import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IbanErratiDuComponent } from './iban-errati-du.component';

describe('IbanErratiDuComponent', () => {
  let component: IbanErratiDuComponent;
  let fixture: ComponentFixture<IbanErratiDuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IbanErratiDuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IbanErratiDuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
