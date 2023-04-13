import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerDomandeUnicheComponent } from './container-domande-uniche.component';

describe('ContainerDomandeUnicheComponent', () => {
  let component: ContainerDomandeUnicheComponent;
  let fixture: ComponentFixture<ContainerDomandeUnicheComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerDomandeUnicheComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerDomandeUnicheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
