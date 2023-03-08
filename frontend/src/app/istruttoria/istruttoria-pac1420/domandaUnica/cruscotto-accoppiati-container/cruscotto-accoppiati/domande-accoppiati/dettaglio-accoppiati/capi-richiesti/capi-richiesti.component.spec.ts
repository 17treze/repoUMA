import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CapiRichiestiComponent } from './capi-richiesti.component';

describe('CapiRichiestiComponent', () => {
  let component: CapiRichiestiComponent;
  let fixture: ComponentFixture<CapiRichiestiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CapiRichiestiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CapiRichiestiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
