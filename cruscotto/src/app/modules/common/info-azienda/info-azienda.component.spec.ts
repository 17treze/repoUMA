import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InfoAziendaComponent } from './info-azienda.component';

describe('InfoAziendaComponent', () => {
  let component: InfoAziendaComponent;
  let fixture: ComponentFixture<InfoAziendaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InfoAziendaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InfoAziendaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
