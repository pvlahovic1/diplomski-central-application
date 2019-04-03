import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProstorijeComponent } from './prostorije.component';

describe('ProstorijeComponent', () => {
  let component: ProstorijeComponent;
  let fixture: ComponentFixture<ProstorijeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProstorijeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProstorijeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
