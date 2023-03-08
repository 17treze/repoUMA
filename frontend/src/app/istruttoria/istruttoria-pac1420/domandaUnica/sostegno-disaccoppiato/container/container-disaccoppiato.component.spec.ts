import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerDisaccoppiatoComponent } from './container-disaccoppiato.component';

describe('ContainerDisaccoppiatoComponent', () => {
  let component: ContainerDisaccoppiatoComponent;
  let fixture: ComponentFixture<ContainerDisaccoppiatoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerDisaccoppiatoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerDisaccoppiatoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
