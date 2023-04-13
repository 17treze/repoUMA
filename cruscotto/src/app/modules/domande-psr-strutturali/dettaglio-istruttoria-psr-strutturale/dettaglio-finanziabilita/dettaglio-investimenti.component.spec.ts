import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioInvestimentiComponent } from './dettaglio-investimenti.component';

describe('DettaglioFinanziabilitaComponent', () => {
  let component: DettaglioInvestimentiComponent;
  let fixture: ComponentFixture<DettaglioInvestimentiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioInvestimentiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioInvestimentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
