import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioDomandaPsrStruttComponent } from './dettaglio-domande-psr-strutturali.component';

describe('DettaglioDomandaComponent', () => {
  let component: DettaglioDomandaPsrStruttComponent;
  let fixture: ComponentFixture<DettaglioDomandaPsrStruttComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioDomandaPsrStruttComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioDomandaPsrStruttComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
