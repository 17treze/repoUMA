import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiestaRevocaMandatoRifiutoDialogComponent } from './richiesta-revoca-mandato-rifiuto-dialog.component';

describe('RichiestaRevocaMandatoRifiutoDialogComponent', () => {
  let component: RichiestaRevocaMandatoRifiutoDialogComponent;
  let fixture: ComponentFixture<RichiestaRevocaMandatoRifiutoDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiestaRevocaMandatoRifiutoDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiestaRevocaMandatoRifiutoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
