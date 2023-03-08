import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltriSuperficiComponent } from './filtri-superfici.component';

describe('FiltriSuperficiComponent', () => {
  let component: FiltriSuperficiComponent;
  let fixture: ComponentFixture<FiltriSuperficiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FiltriSuperficiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FiltriSuperficiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
