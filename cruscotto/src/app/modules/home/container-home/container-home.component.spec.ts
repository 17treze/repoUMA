import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerHomeComponent } from './container-home.component';

describe('ContainerDomandeUnicheComponent', () => {
  let component: ContainerHomeComponent;
  let fixture: ComponentFixture<ContainerHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
