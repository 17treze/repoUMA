import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SintesiDomandeComponent } from './sintesi-domande.component';

describe('SintesiDomandeComponent', () => {
  let component: SintesiDomandeComponent;
  let fixture: ComponentFixture<SintesiDomandeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SintesiDomandeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SintesiDomandeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
