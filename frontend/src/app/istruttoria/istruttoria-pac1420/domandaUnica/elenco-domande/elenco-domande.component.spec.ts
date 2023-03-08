import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElencoDomandeComponent } from './elenco-domande.component';

describe('ElencoDomandeComponent', () => {
  let component: ElencoDomandeComponent;
  let fixture: ComponentFixture<ElencoDomandeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElencoDomandeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElencoDomandeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
