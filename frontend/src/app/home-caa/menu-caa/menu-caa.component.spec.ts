import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuCaaComponent } from './menu-caa.component';

describe('MenuCaaComponent', () => {
  let component: MenuCaaComponent;
  let fixture: ComponentFixture<MenuCaaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MenuCaaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuCaaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
