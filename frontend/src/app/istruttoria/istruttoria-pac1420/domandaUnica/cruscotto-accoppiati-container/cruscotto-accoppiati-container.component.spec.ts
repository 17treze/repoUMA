import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CruscottoAccoppiatiContainerComponent } from './cruscotto-accoppiati-container.component';

describe('CruscottoAccoppiatiContainerComponent', () => {
  let component: CruscottoAccoppiatiContainerComponent;
  let fixture: ComponentFixture<CruscottoAccoppiatiContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CruscottoAccoppiatiContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CruscottoAccoppiatiContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
