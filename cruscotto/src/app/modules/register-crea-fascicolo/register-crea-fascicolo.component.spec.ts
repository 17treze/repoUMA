import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterCreaFascicoloComponent } from './register-crea-fascicolo.component';

describe('RegisterCreaFascicoloComponent', () => {
  let component: RegisterCreaFascicoloComponent;
  let fixture: ComponentFixture<RegisterCreaFascicoloComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisterCreaFascicoloComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterCreaFascicoloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
