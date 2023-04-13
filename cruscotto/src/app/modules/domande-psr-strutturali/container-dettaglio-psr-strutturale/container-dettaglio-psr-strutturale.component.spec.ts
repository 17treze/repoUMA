import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerDettaglioDomandaPsrStruttComponent } from './container-dettaglio-psr-strutturale.component';

describe('ContainerDettaglioDomandaUnicaComponent', () => {
  let component: ContainerDettaglioDomandaPsrStruttComponent;
  let fixture: ComponentFixture<ContainerDettaglioDomandaPsrStruttComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerDettaglioDomandaPsrStruttComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerDettaglioDomandaPsrStruttComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
