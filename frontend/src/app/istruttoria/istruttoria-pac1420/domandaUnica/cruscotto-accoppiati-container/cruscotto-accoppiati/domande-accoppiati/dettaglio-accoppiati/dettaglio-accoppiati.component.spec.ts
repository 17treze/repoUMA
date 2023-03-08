import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioAccoppiatiComponent } from './dettaglio-accoppiati.component';

describe('DettaglioAccoppiatiComponent', () => {
  let component: DettaglioAccoppiatiComponent;
  let fixture: ComponentFixture<DettaglioAccoppiatiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioAccoppiatiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioAccoppiatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
