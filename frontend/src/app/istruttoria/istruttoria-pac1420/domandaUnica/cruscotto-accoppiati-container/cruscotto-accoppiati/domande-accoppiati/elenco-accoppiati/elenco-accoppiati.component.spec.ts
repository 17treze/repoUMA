import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ElencoAccoppiatiComponent } from './elenco-accoppiati.component';

describe('ElencoACZComponent', () => {
  let component: ElencoAccoppiatiComponent;
  let fixture: ComponentFixture<ElencoAccoppiatiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ElencoAccoppiatiComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElencoAccoppiatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
