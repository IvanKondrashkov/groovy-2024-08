import {Component} from '@angular/core';
import {ActivatedRoute, Router, RouterLink, RouterOutlet} from '@angular/router';
import {ActionService} from '../action.service';
import {Action} from '../action';
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
  selector: 'app-action-details',
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
  templateUrl: './action-details.component.html',
  styleUrl: './action-details.component.css'
})
export class ActionDetailsComponent {
  id!: any;
  userId!: any;
  action!: Action;
  isLoadingResults = true;

  constructor(
    private actionService: ActionService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.userId = this.route.snapshot.params['userId'];
    this.findById(this.userId, this.id)
  }

  findById(userId: any, id: any) {
    this.actionService.findById(userId, id).subscribe((res: any) => {
      this.action = res
      console.log(res);
      this.isLoadingResults = false;
    })
  }

  deleteById(userId: any, id: any) {
    this.isLoadingResults = true;
    this.id = id;
    this.actionService.deleteById(userId, id).subscribe(res => {
      this.isLoadingResults = false;
      this.router.navigate(['action/list/' + this.userId]);
      console.log('Event delete successfully!');
    })
  }
}
