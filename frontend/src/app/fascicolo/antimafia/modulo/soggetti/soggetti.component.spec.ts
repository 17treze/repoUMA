import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SoggettiComponent } from './soggetti.component';

describe('SoggettiComponent', () => {
  let component: SoggettiComponent;
  let fixture: ComponentFixture<SoggettiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SoggettiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SoggettiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
