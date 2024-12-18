import {Component} from '@angular/core';
import {ActivatedRoute, Router, RouterLink, RouterOutlet} from '@angular/router';
import {UserService} from '../user.service';
import {User} from '../user';
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
  selector: 'app-user-details',
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
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.css'
})
export class UserDetailsComponent {
  id!: any;
  user!: User;
  isLoadingResults = true;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.findById(this.id)
  }

  findById(id: any) {
    this.userService.findById(id).subscribe((res: any) => {
      this.user = res
      console.log(res);
      this.isLoadingResults = false;
    })
  }

  deleteById(id: any) {
    this.isLoadingResults = true;
    this.id = id;
    this.userService.deleteById(id).subscribe(res => {
      this.isLoadingResults = false;
      this.router.navigate(['user/list']);
      console.log('Event delete successfully!');
    })
  }
}
