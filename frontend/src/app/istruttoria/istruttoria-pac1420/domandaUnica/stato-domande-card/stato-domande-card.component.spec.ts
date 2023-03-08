import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatoDomandeCardComponent } from './stato-domande-card.component';

describe('StatoDomandeCardComponent', () => {
  let component: StatoDomandeCardComponent;
  let fixture: ComponentFixture<StatoDomandeCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatoDomandeCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatoDomandeCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
