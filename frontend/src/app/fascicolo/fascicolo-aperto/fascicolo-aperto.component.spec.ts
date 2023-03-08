import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FascicoloApertoComponent } from './fascicolo-aperto.component';

describe('FascicoloApertoComponent', () => {
  let component: FascicoloApertoComponent;
  let fixture: ComponentFixture<FascicoloApertoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FascicoloApertoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FascicoloApertoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
