import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiedenteComponent } from './richiedente.component';

describe('RichiedenteComponent', () => {
  let component: RichiedenteComponent;
  let fixture: ComponentFixture<RichiedenteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiedenteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiedenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
