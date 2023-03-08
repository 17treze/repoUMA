import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MapNavigatorDialogComponent } from './map-navigator-dialog.component';

describe('MapNavigatorDialogComponent', () => {
  let component: MapNavigatorDialogComponent;
  let fixture: ComponentFixture<MapNavigatorDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MapNavigatorDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapNavigatorDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
