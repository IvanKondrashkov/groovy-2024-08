import {Component} from '@angular/core';
import {ActivatedRoute, Router, RouterLink, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActionService} from '../action.service';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard} from '@angular/material/card';
import {MatError, MatFormField, MatInput} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-action-add',
  standalone: true,
  imports: [
    MatAnchor,
    MatCard,
    MatInput,
    MatError,
    MatButton,
    MatFormField,
    MatProgressSpinner,
    MatIconModule,
    RouterLink,
    RouterOutlet,
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './action-add.component.html',
  styleUrl: './action-add.component.css'
})
export class ActionAddComponent {
  userId!: any;
  taskId!: any;
  form!: FormGroup;
  isLoadingResults = false;

  constructor(
    private actionService: ActionService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.userId = this.route.snapshot.params['userId'];
    this.taskId = this.route.snapshot.params['taskId'];
    this.form = new FormGroup({
      name: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      startDate: new FormControl('', Validators.required),
      endDate: new FormControl('', Validators.required)
    });
  }

  get validate() {
    return this.form.controls;
  }

  submit() {
    this.isLoadingResults = true;
    console.log(this.form.value);
    this.actionService.save(this.form.value, this.userId, this.taskId).subscribe((res: any) => {
      this.isLoadingResults = false;
      console.log('Event created successfully!');
      this.router.navigateByUrl('action/list/' + this.userId);
    })
  }
}
