import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HackPtabMenuComponent } from './hack-ptab-menu.component';

describe('HackPtabMenuComponent', () => {
  let component: HackPtabMenuComponent;
  let fixture: ComponentFixture<HackPtabMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HackPtabMenuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HackPtabMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
