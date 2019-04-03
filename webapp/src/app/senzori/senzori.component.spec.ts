import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SenzoriComponent } from './senzori.component';

describe('SenzoriComponent', () => {
  let component: SenzoriComponent;
  let fixture: ComponentFixture<SenzoriComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SenzoriComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SenzoriComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
