import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelezioneDichiaratoComponent } from './selezione-dichiarato.component';

describe('SelezioneDichiaratoComponent', () => {
  let component: SelezioneDichiaratoComponent;
  let fixture: ComponentFixture<SelezioneDichiaratoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelezioneDichiaratoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelezioneDichiaratoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
