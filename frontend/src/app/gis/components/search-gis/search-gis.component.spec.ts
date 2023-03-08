import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchGisComponent } from './search-gis.component';

describe('SearchGisComponent', () => {
  let component: SearchGisComponent;
  let fixture: ComponentFixture<SearchGisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchGisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchGisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
