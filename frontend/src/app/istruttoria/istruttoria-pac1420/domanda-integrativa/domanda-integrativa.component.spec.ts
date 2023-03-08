import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DomandaIntegrativaComponent } from './domanda-integrativa.component';

describe('DomandaIntegrativaComponent', () => {
  let component: DomandaIntegrativaComponent;
  let fixture: ComponentFixture<DomandaIntegrativaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DomandaIntegrativaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DomandaIntegrativaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
