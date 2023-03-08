import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TitolareComponent } from './titolare.component';

describe('TitolareComponent', () => {
  let component: TitolareComponent;
  let fixture: ComponentFixture<TitolareComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TitolareComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TitolareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
