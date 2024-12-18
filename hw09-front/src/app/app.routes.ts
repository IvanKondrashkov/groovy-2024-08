import {Routes} from '@angular/router';
import {TaskListComponent} from './task/list/task-list.component';
import {TaskDetailsComponent} from './task/details/task-details.component';
import {TaskEditComponent} from './task/edit/task-edit.component';
import {TaskAddComponent} from './task/add/task-add.component';
import {ActionListComponent} from './action/list/action-list.component';
import {ActionDetailsComponent} from './action/details/action-details.component';
import {ActionEditComponent} from './action/edit/action-edit.component';
import {ActionAddComponent} from './action/add/action-add.component';
import {UserListComponent} from './user/list/user-list.component';
import {UserDetailsComponent} from './user/details/user-details.component';
import {UserEditComponent} from './user/edit/user-edit.component';
import {UserAddComponent} from './user/add/user-add.component';
import {LoginComponent} from './auth/login/login.component';

export const routes: Routes = [
  { path: '', redirectTo: 'user-add', pathMatch: 'full'},
  { path: 'user/list', component: UserListComponent },
  { path: 'user-details/:id', component: UserDetailsComponent },
  { path: 'user-add', component: UserAddComponent },
  { path: 'user-edit/:id', component: UserEditComponent },

  { path: 'task/list/:userId', component: TaskListComponent },
  { path: 'task-details/:userId/:id', component: TaskDetailsComponent },
  { path: 'task-add/:userId', component: TaskAddComponent },
  { path: 'task-edit/:userId/:id', component: TaskEditComponent },

  { path: 'action/list/:userId', component: ActionListComponent},
  { path: 'action-details/:userId/:id', component: ActionDetailsComponent },
  { path: 'action-add/:userId/:taskId', component: ActionAddComponent },
  { path: 'action-edit/:userId/:id', component: ActionEditComponent },

  { path: 'login', component: LoginComponent }
];
