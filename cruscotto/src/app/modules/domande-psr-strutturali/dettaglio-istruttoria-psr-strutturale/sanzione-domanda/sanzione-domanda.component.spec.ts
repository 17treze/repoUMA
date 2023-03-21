import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SanzioneDomandaComponent } from './sanzione-domanda.component';

describe('SanzioneDomandaComponent', () => {
  let component: SanzioneDomandaComponent;
  let fixture: ComponentFixture<SanzioneDomandaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SanzioneDomandaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SanzioneDomandaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
