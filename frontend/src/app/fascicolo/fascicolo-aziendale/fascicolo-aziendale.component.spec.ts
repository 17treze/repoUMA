import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FascicoloAziendaleComponent } from './fascicolo-aziendale.component';

describe('FascicoloAziendaleComponent', () => {
  let component: FascicoloAziendaleComponent;
  let fixture: ComponentFixture<FascicoloAziendaleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FascicoloAziendaleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FascicoloAziendaleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
