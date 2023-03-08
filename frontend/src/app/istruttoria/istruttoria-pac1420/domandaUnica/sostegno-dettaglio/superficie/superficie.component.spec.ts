import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SuperficieComponent } from './superficie.component';

describe('SuperficieComponent', () => {
  let component: SuperficieComponent;
  let fixture: ComponentFixture<SuperficieComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SuperficieComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SuperficieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
