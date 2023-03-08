import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DistributoreComponent } from './distributore.component';

describe('DistributoreComponent', () => {
  let component: DistributoreComponent;
  let fixture: ComponentFixture<DistributoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DistributoreComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DistributoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
