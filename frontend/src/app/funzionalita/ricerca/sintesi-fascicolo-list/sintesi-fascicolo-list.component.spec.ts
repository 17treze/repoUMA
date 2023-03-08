import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SintesiFascicoloListComponent } from './sintesi-fascicolo-list.component';

describe('SintesiFascicoloListComponent', () => {
  let component: SintesiFascicoloListComponent;
  let fixture: ComponentFixture<SintesiFascicoloListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SintesiFascicoloListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SintesiFascicoloListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
