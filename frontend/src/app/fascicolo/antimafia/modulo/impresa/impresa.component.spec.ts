import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ImpresaComponent } from './impresa.component';

describe('ImpresaComponent', () => {
  let component: ImpresaComponent;
  let fixture: ComponentFixture<ImpresaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ImpresaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImpresaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
