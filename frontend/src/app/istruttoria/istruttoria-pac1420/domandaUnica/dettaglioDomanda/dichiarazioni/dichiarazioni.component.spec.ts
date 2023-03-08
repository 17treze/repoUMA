import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DichiarazioniComponent } from './dichiarazioni.component';

describe('DichiarazioniComponent', () => {
  let component: DichiarazioniComponent;
  let fixture: ComponentFixture<DichiarazioniComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DichiarazioniComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DichiarazioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
