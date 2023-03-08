import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SommarioIstruttoria1420Component } from './sommario-istruttoria1420.component';

describe('SommarioIstruttoria1420Component', () => {
  let component: SommarioIstruttoria1420Component;
  let fixture: ComponentFixture<SommarioIstruttoria1420Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SommarioIstruttoria1420Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SommarioIstruttoria1420Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
