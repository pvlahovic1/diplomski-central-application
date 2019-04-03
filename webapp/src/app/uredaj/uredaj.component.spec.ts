import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UredajComponent } from './uredaj.component';

describe('UredajComponent', () => {
  let component: UredajComponent;
  let fixture: ComponentFixture<UredajComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UredajComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UredajComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
