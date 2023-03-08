import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PersoneConCaricaComponent } from './persone-con-carica.component';

describe('SuperficiImpegnateComponent', () => {
  let component: PersoneConCaricaComponent;
  let fixture: ComponentFixture<PersoneConCaricaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PersoneConCaricaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersoneConCaricaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
