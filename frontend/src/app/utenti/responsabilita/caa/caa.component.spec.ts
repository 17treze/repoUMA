import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CAAComponent } from './caa.component';

describe('CAAComponent', () => {
  let component: CAAComponent;
  let fixture: ComponentFixture<CAAComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CAAComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CAAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
