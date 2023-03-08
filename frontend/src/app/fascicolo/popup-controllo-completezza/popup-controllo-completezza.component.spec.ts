import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PopupControlloCompletezzaComponent } from './popup-controllo-completezza.component';

describe('PopupControlloCompletezzaAsyncComponent', () => {
  let component: PopupControlloCompletezzaComponent;
  let fixture: ComponentFixture<PopupControlloCompletezzaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PopupControlloCompletezzaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopupControlloCompletezzaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
