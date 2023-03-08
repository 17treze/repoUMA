import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiesteRifiutateComponent } from './richieste-rifiutate.component';

describe('RichiesteRifiutateComponent', () => {
  let component: RichiesteRifiutateComponent;
  let fixture: ComponentFixture<RichiesteRifiutateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiesteRifiutateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiesteRifiutateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
