import {Component} from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {UserService} from '../user.service';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard} from '@angular/material/card';
import {MatError, MatFormField, MatInput} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-user-add',
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
  templateUrl: './user-add.component.html',
  styleUrl: './user-add.component.css'
})
export class UserAddComponent {
  form!: FormGroup;
  isLoadingResults = false;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit() {
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
    this.userService.save(this.form.value).subscribe((res: any) => {
      this.isLoadingResults = false;
      console.log('Event created successfully!');
      this.router.navigateByUrl('login');
    })
  }
}
