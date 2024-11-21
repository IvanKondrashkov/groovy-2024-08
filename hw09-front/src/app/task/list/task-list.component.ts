import {Component, ViewChild} from '@angular/core';
import {MatSort} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {
  MatCell,
  MatRow,
  MatTable,
  MatColumnDef,
  MatHeaderRow,
  MatHeaderCell,
  MatTableDataSource, MatHeaderCellDef, MatCellDef, MatHeaderRowDef, MatRowDef
} from '@angular/material/table';
import {TaskService} from '../task.service';
import {Task} from '../task';
import {MatAnchor} from '@angular/material/button';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {CommonModule} from '@angular/common';
import {RouterLink, RouterOutlet} from '@angular/router';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-task-list',
  standalone: true,
  templateUrl: './task-list.component.html',
  imports: [
    MatAnchor,
    MatSort,
    MatTable,
    MatRow,
    MatCell,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatIconModule,
    MatPaginator,
    MatProgressSpinner,
    CommonModule,
    RouterOutlet,
    RouterLink,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRowDef,
    MatRowDef
  ],
  styleUrl: './task-list.component.css'
})
export class TaskListComponent {
  tasks!: Task[];
  displayedColumns: string[] = ['name', 'description', 'startDate', 'endDate'];
  dataSource: MatTableDataSource<Task> = new MatTableDataSource<Task>();
  // @ts-ignore
  @ViewChild(MatPaginator) paginator: MatPaginator;
  // @ts-ignore
  @ViewChild(MatSort) sort: MatSort;
  isLoadingResults = true;

  constructor(
    private taskService: TaskService
  ) {}

  ngOnInit() {
    this.taskService.findAll().subscribe((res: Task[]) => {
      this.dataSource.data = res;
      this.tasks = res;
      console.log('this.dataSource = ', this.dataSource.data);
      this.isLoadingResults = false;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      console.log(this.tasks);
    })
  }
}
