import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GruppiFabbricatoComponent } from './gruppi-fabbricato.component';

describe('GruppiFabbricatoComponent', () => {
  let component: GruppiFabbricatoComponent;
  let fixture: ComponentFixture<GruppiFabbricatoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GruppiFabbricatoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GruppiFabbricatoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
