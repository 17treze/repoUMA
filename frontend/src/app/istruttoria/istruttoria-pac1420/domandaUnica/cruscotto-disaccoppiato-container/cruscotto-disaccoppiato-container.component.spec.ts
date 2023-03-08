import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CruscottoDisaccoppiatoContainerComponent } from './cruscotto-disaccoppiato-container.component';

describe('CruscottoDisaccoppiatoContainerComponent', () => {
  let component: CruscottoDisaccoppiatoContainerComponent;
  let fixture: ComponentFixture<CruscottoDisaccoppiatoContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CruscottoDisaccoppiatoContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CruscottoDisaccoppiatoContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
