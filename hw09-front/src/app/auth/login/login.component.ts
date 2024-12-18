import {Component} from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatCard} from '@angular/material/card';
import {MatError, MatFormField, MatInput} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {AuthService} from '../auth.service';
import {RefreshInfo} from '../refresh-info';

@Component({
  selector: 'app-login',
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
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  refreshInfo!: RefreshInfo;
  form!: FormGroup;
  isLoadingResults = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.form = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
    });
  }

  get validate() {
    return this.form.controls;
  }

  refreshToken(refreshInfo: RefreshInfo) {
    this.isLoadingResults = true;
    console.log(refreshInfo);
    this.authService.refreshToken(refreshInfo).subscribe((res: any) => {
      this.isLoadingResults = false;
      localStorage.setItem('access_token', res.access_token);
      console.log('Event refresh token successfully!');
    })
  }

  submit() {
    this.isLoadingResults = true;
    console.log(this.form.value);
    this.authService.login(this.form.value).subscribe((res: any) => {
      this.isLoadingResults = false;
      localStorage.setItem('access_token', res.access_token);
      console.log('Event login successfully!');
      this.router.navigateByUrl('user/list');
    })
  }
}
