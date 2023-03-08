import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltriDisaccoppiatoComponent } from './filtri-disaccoppiato.component';

describe('FiltriDisaccoppiatoComponent', () => {
  let component: FiltriDisaccoppiatoComponent;
  let fixture: ComponentFixture<FiltriDisaccoppiatoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FiltriDisaccoppiatoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FiltriDisaccoppiatoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
