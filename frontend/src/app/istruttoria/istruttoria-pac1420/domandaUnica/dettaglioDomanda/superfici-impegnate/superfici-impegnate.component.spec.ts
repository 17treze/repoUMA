import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SuperficiImpegnateComponent } from './superfici-impegnate.component';

describe('SuperficiImpegnateComponent', () => {
  let component: SuperficiImpegnateComponent;
  let fixture: ComponentFixture<SuperficiImpegnateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SuperficiImpegnateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SuperficiImpegnateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
