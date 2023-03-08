import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiestaRevocaMandatoDaValutareComponent } from './richiesta-revoca-mandato-da-valutare.component';

describe('RichiestaRevocaMandatoDaValutareComponent', () => {
  let component: RichiestaRevocaMandatoDaValutareComponent;
  let fixture: ComponentFixture<RichiestaRevocaMandatoDaValutareComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiestaRevocaMandatoDaValutareComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiestaRevocaMandatoDaValutareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
