import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PopupCompletaTrasferimentoInAppagComponent } from './popup-completa-trasferimento-in-appag.component';

describe('PopupCompletaTrasferimentoInAppagComponent', () => {
  let component: PopupCompletaTrasferimentoInAppagComponent;
  let fixture: ComponentFixture<PopupCompletaTrasferimentoInAppagComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PopupCompletaTrasferimentoInAppagComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopupCompletaTrasferimentoInAppagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
