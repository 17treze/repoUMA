import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerSuperficiComponent } from './container-superfici.component';

describe('ContainerSuperficiComponent', () => {
  let component: ContainerSuperficiComponent;
  let fixture: ComponentFixture<ContainerSuperficiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerSuperficiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerSuperficiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
