import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalitaPagamentoComponent } from './modalita-pagamento.component';

describe('ModalitaPagamentoComponent', () => {
  let component: ModalitaPagamentoComponent;
  let fixture: ComponentFixture<ModalitaPagamentoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalitaPagamentoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalitaPagamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
