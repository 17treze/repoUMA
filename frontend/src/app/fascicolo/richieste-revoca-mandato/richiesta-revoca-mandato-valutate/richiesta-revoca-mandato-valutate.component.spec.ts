import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiestaRevocaMandatoValutateComponent } from './richiesta-revoca-mandato-valutate.component';

describe('RichiestaRevocaMandatoValutateComponent', () => {
  let component: RichiestaRevocaMandatoValutateComponent;
  let fixture: ComponentFixture<RichiestaRevocaMandatoValutateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiestaRevocaMandatoValutateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiestaRevocaMandatoValutateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
