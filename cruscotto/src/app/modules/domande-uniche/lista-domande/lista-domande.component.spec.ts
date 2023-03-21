import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaDomandeComponent } from './lista-domande.component';

describe('ListaDomandeComponent', () => {
  let component: ListaDomandeComponent;
  let fixture: ComponentFixture<ListaDomandeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListaDomandeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListaDomandeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
