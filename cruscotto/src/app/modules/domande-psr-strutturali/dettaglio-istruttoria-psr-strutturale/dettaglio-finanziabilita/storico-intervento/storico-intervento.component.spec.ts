import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StoricoInterventoComponent } from './storico-intervento.component';

describe('StoricoInterventoComponent', () => {
  let component: StoricoInterventoComponent;
  let fixture: ComponentFixture<StoricoInterventoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StoricoInterventoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StoricoInterventoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
