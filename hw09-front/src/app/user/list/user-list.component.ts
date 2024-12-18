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
import {UserService} from '../user.service';
import {User} from '../user';
import {MatAnchor} from '@angular/material/button';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {CommonModule} from '@angular/common';
import {RouterLink, RouterOutlet} from '@angular/router';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-user-list',
  standalone: true,
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
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent {
  users!: User[];
  displayedColumns: string[] = ['firstName', 'lastName', 'email'];
  dataSource: MatTableDataSource<User> = new MatTableDataSource<User>();
  // @ts-ignore
  @ViewChild(MatPaginator) paginator: MatPaginator;
  // @ts-ignore
  @ViewChild(MatSort) sort: MatSort;
  isLoadingResults = true;

  constructor(
    private userService: UserService
  ) {}

  ngOnInit() {
    this.userService.findAll().subscribe((res: User[]) => {
      this.dataSource.data = res;
      this.users = res;
      console.log('this.dataSource = ', this.dataSource.data);
      this.isLoadingResults = false;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      console.log(this.users);
    })
  }
}
