import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScalaComponent } from './scala.component';

describe('ScalaComponent', () => {
  let component: ScalaComponent;
  let fixture: ComponentFixture<ScalaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScalaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScalaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
