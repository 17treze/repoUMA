import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CapiAmmessiComponent } from './capi-ammessi.component';

describe('CapiAmmessiComponent', () => {
  let component: CapiAmmessiComponent;
  let fixture: ComponentFixture<CapiAmmessiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CapiAmmessiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CapiAmmessiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
