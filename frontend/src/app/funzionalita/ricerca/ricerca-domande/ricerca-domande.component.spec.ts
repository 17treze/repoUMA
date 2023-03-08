import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RicercaDomandeComponent } from './ricerca-domande.component';


describe('RicercaDomandeComponent', () => {
  let component: RicercaDomandeComponent;
  let fixture: ComponentFixture<RicercaDomandeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RicercaDomandeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RicercaDomandeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
