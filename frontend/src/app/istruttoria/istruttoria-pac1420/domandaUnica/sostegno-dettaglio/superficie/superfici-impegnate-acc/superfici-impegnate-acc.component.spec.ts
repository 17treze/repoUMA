import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SuperficiImpegnateAccComponent } from './superfici-impegnate-acc.component';

describe('SuperficiImpegnateAccComponent', () => {
  let component: SuperficiImpegnateAccComponent;
  let fixture: ComponentFixture<SuperficiImpegnateAccComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SuperficiImpegnateAccComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SuperficiImpegnateAccComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
