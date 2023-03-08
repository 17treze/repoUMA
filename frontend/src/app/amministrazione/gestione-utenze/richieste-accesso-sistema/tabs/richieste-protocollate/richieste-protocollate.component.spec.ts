import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiesteProtocollateComponent } from './richieste-protocollate.component';

describe('RichiesteProtocollateComponent', () => {
  let component: RichiesteProtocollateComponent;
  let fixture: ComponentFixture<RichiesteProtocollateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiesteProtocollateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiesteProtocollateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
