import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiesteAccessoSistemaTabComponent } from './richieste-accesso-sistema-tab.component';

describe('RichiesteAccessoSistemaTabComponent', () => {
  let component: RichiesteAccessoSistemaTabComponent;
  let fixture: ComponentFixture<RichiesteAccessoSistemaTabComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiesteAccessoSistemaTabComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiesteAccessoSistemaTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
