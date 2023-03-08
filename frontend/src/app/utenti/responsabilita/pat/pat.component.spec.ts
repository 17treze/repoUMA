import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PATComponent } from './pat.component';

describe('PATComponent', () => {
  let component: PATComponent;
  let fixture: ComponentFixture<PATComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PATComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PATComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
