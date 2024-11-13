import {Component} from '@angular/core';
import {ActivatedRoute, Router, RouterLink, RouterOutlet} from '@angular/router';
import {TaskService} from '../task.service';
import {Task} from '../task';
import {CommonModule} from '@angular/common';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader,
  MatCardTitle
} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {MatAnchor} from '@angular/material/button';
import {MatProgressSpinner} from '@angular/material/progress-spinner';

@Component({
  selector: 'app-task-details',
  standalone: true,
  imports: [
    MatIcon,
    MatCardContent,
    MatAnchor,
    MatCard,
    MatCardTitle,
    MatCardHeader,
    MatCardActions,
    MatProgressSpinner,
    CommonModule,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './task-details.component.html',
  styleUrl: './task-details.component.css'
})
export class TaskDetailsComponent {
  id!: any;
  task!: Task;
  isLoadingResults = true;

  constructor(
    private taskService: TaskService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.findById(this.id)
  }

  findById(id: any) {
    this.taskService.findById(id).subscribe((res: any) => {
      this.task = res
      console.log(res);
      this.isLoadingResults = false;
    })
  }

  deleteById(id: any) {
    this.isLoadingResults = true;
    this.id = id;
    this.taskService.deleteById(id).subscribe(res => {
      this.isLoadingResults = false;
      this.router.navigate(['/task/list']);
      console.log('Event delete successfully!');
    })
  }
}