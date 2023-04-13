import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FascicoliValidatiFilterDto } from './fascicoli-validati-filter.component';

describe('FascicoliValidatiFilterComponent', () => {
  let component: FascicoliValidatiFilterDto;
  let fixture: ComponentFixture<FascicoliValidatiFilterDto>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FascicoliValidatiFilterDto ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FascicoliValidatiFilterDto);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
