import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelezioneSuoloComponent } from './selezione-suolo-area.component';

describe('SelezioneSuoloComponent', () => {
  let component: SelezioneSuoloComponent;
  let fixture: ComponentFixture<SelezioneSuoloComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelezioneSuoloComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelezioneSuoloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
