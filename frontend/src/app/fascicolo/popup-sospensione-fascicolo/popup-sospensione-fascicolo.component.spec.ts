import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PopupSospensioneFascicoloComponent } from './popup-sospensione-fascicolo.component';

describe('PopupSospensioneFascicoloComponent', () => {
  let component: PopupSospensioneFascicoloComponent;
  let fixture: ComponentFixture<PopupSospensioneFascicoloComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PopupSospensioneFascicoloComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopupSospensioneFascicoloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
