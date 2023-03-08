import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisaccoppiatoControlliSuperatiComponent } from './disaccoppiato-controlli-superati.component';

describe('DisaccoppiatoControlliSuperatiComponent', () => {
  let component: DisaccoppiatoControlliSuperatiComponent;
  let fixture: ComponentFixture<DisaccoppiatoControlliSuperatiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DisaccoppiatoControlliSuperatiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisaccoppiatoControlliSuperatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
