import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DomandeAccoppiatiComponent } from './domande-accoppiati.component';

describe('DomandeAccoppiatiComponent', () => {
  let component: DomandeAccoppiatiComponent;
  let fixture: ComponentFixture<DomandeAccoppiatiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DomandeAccoppiatiComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DomandeAccoppiatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
