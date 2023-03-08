import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AltriEntiComponent } from './altri-enti.component';

describe('AltriEntiComponent', () => {
  let component: AltriEntiComponent;
  let fixture: ComponentFixture<AltriEntiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AltriEntiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AltriEntiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
