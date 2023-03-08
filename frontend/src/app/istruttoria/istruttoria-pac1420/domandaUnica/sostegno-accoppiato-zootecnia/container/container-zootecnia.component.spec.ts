import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerZootecniaComponent } from './container-zootecnia.component';

describe('ContainerZootecniaComponent', () => {
  let component: ContainerZootecniaComponent;
  let fixture: ComponentFixture<ContainerZootecniaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerZootecniaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerZootecniaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
