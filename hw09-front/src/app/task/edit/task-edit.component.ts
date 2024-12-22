import { Component } from '@angular/core';
import {ActivatedRoute, Router, RouterLink, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {TaskService} from '../task.service';
import {Task} from '../task';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard} from '@angular/material/card';
import {MatError, MatFormField, MatInput} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-task-edit',
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
  templateUrl: './task-edit.component.html',
  styleUrl: './task-edit.component.css'
})
export class TaskEditComponent {
  id!: any;
  userId!: any;
  task!: Task;
  form!: FormGroup;
  isLoadingResults = false;

  constructor(
    private taskService: TaskService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.userId = this.route.snapshot.params['userId'];
    this.taskService.findById(this.userId, this.id).subscribe((res: Task) => {
      this.task = res;
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
    console.log(this.form.value);
    this.taskService.updateById(this.userId, this.id, this.form.value).subscribe((res: any) => {
      this.isLoadingResults = false;
      console.log('Event update successfully!');
      this.router.navigateByUrl('task/list/' + this.userId);
    })
  }
}
