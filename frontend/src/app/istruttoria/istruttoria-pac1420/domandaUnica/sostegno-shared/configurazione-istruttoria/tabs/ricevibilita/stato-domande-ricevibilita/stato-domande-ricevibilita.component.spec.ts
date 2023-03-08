import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatoDomandeRicevibilitaComponent } from './stato-domande-ricevibilita.component';

describe('StatoDomandeCardComponent', () => {
  let component: StatoDomandeRicevibilitaComponent;
  let fixture: ComponentFixture<StatoDomandeRicevibilitaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatoDomandeRicevibilitaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatoDomandeRicevibilitaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
