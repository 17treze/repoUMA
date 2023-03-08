import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StoricoUtenzaComponent } from './storico-utenza.component';

describe('StoricoUtenzaComponent', () => {
  let component: StoricoUtenzaComponent;
  let fixture: ComponentFixture<StoricoUtenzaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StoricoUtenzaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StoricoUtenzaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
