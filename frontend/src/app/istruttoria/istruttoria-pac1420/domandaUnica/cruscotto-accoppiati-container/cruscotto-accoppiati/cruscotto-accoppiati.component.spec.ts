import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CruscottoAccoppiatiComponent } from './cruscotto-accoppiati.component';

describe('CruscottoAccoppiatoZootecniaComponent', () => {
  let component: CruscottoAccoppiatiComponent;
  let fixture: ComponentFixture<CruscottoAccoppiatiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CruscottoAccoppiatiComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CruscottoAccoppiatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
