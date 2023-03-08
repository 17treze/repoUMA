import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CruscottoDisaccoppiatoComponent } from './cruscotto-disaccoppiato.component';

describe('CruscottoDisaccoppiatoComponent', () => {
  let component: CruscottoDisaccoppiatoComponent;
  let fixture: ComponentFixture<CruscottoDisaccoppiatoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CruscottoDisaccoppiatoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CruscottoDisaccoppiatoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
