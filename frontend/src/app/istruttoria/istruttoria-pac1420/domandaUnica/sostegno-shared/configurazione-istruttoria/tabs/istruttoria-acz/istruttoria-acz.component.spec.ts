import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IstruttoriaAczComponent } from './istruttoria-acz.component';

describe('IstruttoriaAczComponent', () => {
  let component: IstruttoriaAczComponent;
  let fixture: ComponentFixture<IstruttoriaAczComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IstruttoriaAczComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IstruttoriaAczComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
