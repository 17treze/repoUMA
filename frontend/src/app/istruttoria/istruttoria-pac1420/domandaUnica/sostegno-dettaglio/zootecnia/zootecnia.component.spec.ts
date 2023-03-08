import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ZootecniaComponent } from './zootecnia.component';

describe('ZootecniaComponent', () => {
  let component: ZootecniaComponent;
  let fixture: ComponentFixture<ZootecniaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ZootecniaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ZootecniaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
