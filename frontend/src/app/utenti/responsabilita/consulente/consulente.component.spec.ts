import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsulenteComponent } from './consulente.component';

describe('ConsulenteComponent', () => {
  let component: ConsulenteComponent;
  let fixture: ComponentFixture<ConsulenteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConsulenteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsulenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
