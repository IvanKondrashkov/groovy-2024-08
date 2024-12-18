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
import {ActionService} from '../action.service';
import {Action} from '../action';
import {MatAnchor} from '@angular/material/button';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterLink, RouterOutlet} from '@angular/router';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-action-list',
  standalone: true,
  templateUrl: './action-list.component.html',
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
  styleUrl: './action-list.component.css'
})
export class ActionListComponent {
  userId!: any;
  actions!: Action[];
  displayedColumns: string[] = ['name', 'description', 'startDate', 'endDate'];
  dataSource: MatTableDataSource<Action> = new MatTableDataSource<Action>();
  // @ts-ignore
  @ViewChild(MatPaginator) paginator: MatPaginator;
  // @ts-ignore
  @ViewChild(MatSort) sort: MatSort;
  isLoadingResults = true;

  constructor(
    private actionService: ActionService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.userId = this.route.snapshot.params['userId'];
    this.actionService.findAll(this.userId).subscribe((res: Action[]) => {
      this.dataSource.data = res;
      this.actions = res;
      console.log('this.dataSource = ', this.dataSource.data);
      this.isLoadingResults = false;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      console.log(this.actions);
    })
  }
}
