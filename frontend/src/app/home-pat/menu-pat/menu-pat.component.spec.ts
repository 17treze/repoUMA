import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuPatComponent } from './menu-pat.component';

describe('MenuPatComponent', () => {
  let component: MenuPatComponent;
  let fixture: ComponentFixture<MenuPatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MenuPatComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuPatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
