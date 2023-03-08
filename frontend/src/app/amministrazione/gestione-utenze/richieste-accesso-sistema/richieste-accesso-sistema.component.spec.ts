import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiesteAccessoSistemaComponent } from './richieste-accesso-sistema.component';

describe('RichiesteAccessoSistemaComponent', () => {
  let component: RichiesteAccessoSistemaComponent;
  let fixture: ComponentFixture<RichiesteAccessoSistemaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiesteAccessoSistemaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiesteAccessoSistemaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
