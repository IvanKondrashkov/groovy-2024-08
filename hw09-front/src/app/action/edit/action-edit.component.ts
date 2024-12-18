import {Component} from '@angular/core';
import {ActivatedRoute, Router, RouterLink, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActionService} from '../action.service';
import {Action} from '../action';
import {Task} from '../../task/task';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard} from '@angular/material/card';
import {MatError, MatFormField, MatInput} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-action-edit',
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
  templateUrl: './action-edit.component.html',
  styleUrl: './action-edit.component.css'
})
export class ActionEditComponent {
  id!: any;
  userId!: any;
  task!: Task
  action!: Action;
  form!: FormGroup;
  isLoadingResults = false;

  constructor(
    private actionService: ActionService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.userId = this.route.snapshot.params['userId'];
    this.actionService.findById(this.userId, this.id).subscribe((res: Action) => {
      this.action = res;
      if (res.task) {
        this.task = res.task
      }
    });

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
    this.form.value['task'] = this.task
    console.log(this.form.value);
    this.actionService.updateById(this.userId, this.id, this.form.value).subscribe((res: any) => {
      this.isLoadingResults = false;
      console.log('Event update successfully!');
      this.router.navigateByUrl('action/list/' + this.userId);
    })
  }
}
