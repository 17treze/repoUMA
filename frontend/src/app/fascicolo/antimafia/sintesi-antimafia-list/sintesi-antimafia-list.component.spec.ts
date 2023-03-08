import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SintesiAntimafiaListComponent } from './sintesi-antimafia-list.component';

describe('SintesiAntimafiaListComponent', () => {
  let component: SintesiAntimafiaListComponent;
  let fixture: ComponentFixture<SintesiAntimafiaListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SintesiAntimafiaListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SintesiAntimafiaListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
