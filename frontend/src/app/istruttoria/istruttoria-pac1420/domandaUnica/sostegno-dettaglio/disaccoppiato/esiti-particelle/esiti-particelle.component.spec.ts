import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EsitiParticelleComponent } from './esiti-particelle.component';

describe('EsitiParticelleComponent', () => {
  let component: EsitiParticelleComponent;
  let fixture: ComponentFixture<EsitiParticelleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EsitiParticelleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EsitiParticelleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
