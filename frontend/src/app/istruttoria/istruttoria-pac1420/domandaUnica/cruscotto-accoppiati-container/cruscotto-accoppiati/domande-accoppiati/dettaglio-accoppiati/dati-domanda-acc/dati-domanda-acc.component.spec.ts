import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiDomandaAccComponent } from './dati-domanda-acc.component';

describe('DatiDomandaAccComponent', () => {
  let component: DatiDomandaAccComponent;
  let fixture: ComponentFixture<DatiDomandaAccComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiDomandaAccComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiDomandaAccComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
