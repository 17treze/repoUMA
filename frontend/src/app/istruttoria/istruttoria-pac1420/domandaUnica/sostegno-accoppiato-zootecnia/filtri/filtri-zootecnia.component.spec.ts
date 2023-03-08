import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltriZootecniaComponent } from './filtri-zootecnia.component';

describe('FiltriZootecniaComponent', () => {
  let component: FiltriZootecniaComponent;
  let fixture: ComponentFixture<FiltriZootecniaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FiltriZootecniaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FiltriZootecniaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
