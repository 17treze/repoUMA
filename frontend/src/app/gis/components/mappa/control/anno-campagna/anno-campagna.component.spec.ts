import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnoCampagnaComponent } from './anno-campagna.component';

describe('AnnoCampagnaComponent', () => {
  let component: AnnoCampagnaComponent;
  let fixture: ComponentFixture<AnnoCampagnaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnnoCampagnaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnnoCampagnaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
