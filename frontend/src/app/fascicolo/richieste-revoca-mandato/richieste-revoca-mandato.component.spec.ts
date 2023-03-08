import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiesteRevocaMandatoComponent } from './richieste-revoca-mandato.component';

describe('RichiesteRevocaMandatoComponent', () => {
  let component: RichiesteRevocaMandatoComponent;
  let fixture: ComponentFixture<RichiesteRevocaMandatoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiesteRevocaMandatoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiesteRevocaMandatoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
