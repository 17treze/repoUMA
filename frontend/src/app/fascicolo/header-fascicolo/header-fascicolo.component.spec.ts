import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderFascicoloComponent } from './header-fascicolo.component';

describe('HeaderFascicoloComponent', () => {
  let component: HeaderFascicoloComponent;
  let fixture: ComponentFixture<HeaderFascicoloComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeaderFascicoloComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderFascicoloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
