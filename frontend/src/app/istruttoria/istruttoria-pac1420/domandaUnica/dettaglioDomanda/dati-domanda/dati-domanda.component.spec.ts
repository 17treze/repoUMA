import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiDomandaComponent } from './dati-domanda.component';

describe('DatiDomandaComponent', () => {
  let component: DatiDomandaComponent;
  let fixture: ComponentFixture<DatiDomandaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiDomandaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiDomandaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
