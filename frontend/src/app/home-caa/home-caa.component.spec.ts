import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeCAAComponent } from './home-caa.component';

describe('HomeCAAComponent', () => {
  let component: HomeCAAComponent;
  let fixture: ComponentFixture<HomeCAAComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeCAAComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeCAAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
