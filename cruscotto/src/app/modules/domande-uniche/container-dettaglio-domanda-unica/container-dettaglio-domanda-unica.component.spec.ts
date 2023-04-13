import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerDettaglioDomandaUnicaComponent } from './container-dettaglio-domanda-unica.component';

describe('ContainerDettaglioDomandaUnicaComponent', () => {
  let component: ContainerDettaglioDomandaUnicaComponent;
  let fixture: ComponentFixture<ContainerDettaglioDomandaUnicaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerDettaglioDomandaUnicaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerDettaglioDomandaUnicaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
