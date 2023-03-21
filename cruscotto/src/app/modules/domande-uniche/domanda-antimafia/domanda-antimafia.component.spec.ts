import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DomandaAntimafiaComponent } from './domanda-antimafia.component';

describe('DomandaAntimafiaComponent', () => {
  let component: DomandaAntimafiaComponent;
  let fixture: ComponentFixture<DomandaAntimafiaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DomandaAntimafiaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DomandaAntimafiaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
