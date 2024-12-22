import {Component} from '@angular/core';
import {ActivatedRoute, Router, RouterLink, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {UserService} from '../user.service';
import {User} from '../user';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard} from '@angular/material/card';
import {MatError, MatFormField, MatInput} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-user-edit',
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
  templateUrl: './user-edit.component.html',
  styleUrl: './user-edit.component.css'
})
export class UserEditComponent {
  id!: any;
  user!: User;
  form!: FormGroup;
  isLoadingResults = false;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.userService.findById(this.id).subscribe((res: User) => {
      this.user = res;
    });

    this.form = new FormGroup({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      login: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      email: new FormControl('', Validators.required)
    });
  }

  get validate() {
    return this.form.controls;
  }

  submit() {
    this.isLoadingResults = true;
    console.log(this.form.value);
    this.userService.updateById(this.id, this.form.value).subscribe((res: any) => {
      this.isLoadingResults = false;
      console.log('Event update successfully!');
      this.router.navigateByUrl('user/list');
    })
  }
}
