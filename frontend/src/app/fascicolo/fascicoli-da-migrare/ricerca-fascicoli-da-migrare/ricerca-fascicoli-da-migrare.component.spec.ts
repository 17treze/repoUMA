import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RicercaFascicoliDaMigrareComponent } from './ricerca-fascicoli-da-migrare.component';

describe('RicercaFascicoliDaMigrareComponent', () => {
  let component: RicercaFascicoliDaMigrareComponent;
  let fixture: ComponentFixture<RicercaFascicoliDaMigrareComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RicercaFascicoliDaMigrareComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RicercaFascicoliDaMigrareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
