import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IstruttoriaDisaccoppiatoComponent } from './istruttoria-disaccoppiato.component';

describe('IstruttoriaDisaccoppiatoComponent', () => {
  let component: IstruttoriaDisaccoppiatoComponent;
  let fixture: ComponentFixture<IstruttoriaDisaccoppiatoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IstruttoriaDisaccoppiatoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IstruttoriaDisaccoppiatoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
