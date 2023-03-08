import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioUtenzaComponent } from './dettaglio-utenza.component';

describe('DettaglioUtenzaComponent', () => {
  let component: DettaglioUtenzaComponent;
  let fixture: ComponentFixture<DettaglioUtenzaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioUtenzaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioUtenzaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
