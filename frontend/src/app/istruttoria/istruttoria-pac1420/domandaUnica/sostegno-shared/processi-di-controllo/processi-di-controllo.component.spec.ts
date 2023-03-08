import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessiDiControlloComponent } from './processi-di-controllo.component';

describe('ProcessiDiControlloComponent', () => {
  let component: ProcessiDiControlloComponent;
  let fixture: ComponentFixture<ProcessiDiControlloComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProcessiDiControlloComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcessiDiControlloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
