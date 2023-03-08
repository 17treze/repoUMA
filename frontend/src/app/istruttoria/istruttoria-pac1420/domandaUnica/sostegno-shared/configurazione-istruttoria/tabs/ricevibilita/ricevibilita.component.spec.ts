import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RicevibilitaComponent } from './ricevibilita.component';

describe('RicevibilitaComponent', () => {
  let component: RicevibilitaComponent;
  let fixture: ComponentFixture<RicevibilitaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RicevibilitaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RicevibilitaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
