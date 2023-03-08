import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IstruttoriaAcsComponent } from './istruttoria-acs.component';

describe('IstruttoriaAcsComponent', () => {
  let component: IstruttoriaAcsComponent;
  let fixture: ComponentFixture<IstruttoriaAcsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IstruttoriaAcsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IstruttoriaAcsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
