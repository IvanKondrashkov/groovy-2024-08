import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ActionEditComponent} from './action-edit.component';

describe('EditComponent', () => {
  let component: ActionEditComponent;
  let fixture: ComponentFixture<ActionEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActionEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActionEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
