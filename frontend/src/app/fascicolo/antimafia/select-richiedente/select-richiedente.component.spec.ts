import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectRichiedenteComponent } from './select-richiedente.component';

describe('SelectRichiedenteComponent', () => {
  let component: SelectRichiedenteComponent;
  let fixture: ComponentFixture<SelectRichiedenteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectRichiedenteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectRichiedenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
