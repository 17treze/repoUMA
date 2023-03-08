import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePatComponent } from './home-pat.component';

describe('HomePatComponent', () => {
  let component: HomePatComponent;
  let fixture: ComponentFixture<HomePatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomePatComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomePatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
