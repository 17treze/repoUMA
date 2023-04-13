import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaCuaComponent } from './lista-cuaa.component';

describe('ListaCuaComponent', () => {
  let component: ListaCuaComponent;
  let fixture: ComponentFixture<ListaCuaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListaCuaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListaCuaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
