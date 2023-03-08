import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UnitaTecnicoEconomicheComponent } from './unita-tecnico-economiche.component';

describe('SuperficiImpegnateComponent', () => {
  let component: UnitaTecnicoEconomicheComponent;
  let fixture: ComponentFixture<UnitaTecnicoEconomicheComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UnitaTecnicoEconomicheComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UnitaTecnicoEconomicheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
