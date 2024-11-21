import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ActionAddComponent} from './action-add.component';

describe('CreateComponent', () => {
  let component: ActionAddComponent;
  let fixture: ComponentFixture<ActionAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActionAddComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActionAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
