import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultsGisComponent } from './results-gis.component';

describe('ResultsGisComponent', () => {
  let component: ResultsGisComponent;
  let fixture: ComponentFixture<ResultsGisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResultsGisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResultsGisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
