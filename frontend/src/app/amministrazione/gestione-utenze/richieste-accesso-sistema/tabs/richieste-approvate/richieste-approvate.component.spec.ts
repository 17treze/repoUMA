import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiesteApprovateComponent } from './richieste-approvate.component';

describe('RichiesteApprovateComponent', () => {
  let component: RichiesteApprovateComponent;
  let fixture: ComponentFixture<RichiesteApprovateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiesteApprovateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiesteApprovateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
