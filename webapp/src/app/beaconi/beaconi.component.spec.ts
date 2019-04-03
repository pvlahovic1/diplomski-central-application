import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BeaconiComponent } from './beaconi.component';

describe('BeaconiComponent', () => {
  let component: BeaconiComponent;
  let fixture: ComponentFixture<BeaconiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BeaconiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BeaconiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
