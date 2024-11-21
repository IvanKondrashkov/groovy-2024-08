import {Routes} from '@angular/router';
import {TaskListComponent} from './task/list/task-list.component';
import {TaskDetailsComponent} from './task/details/task-details.component';
import {TaskAddComponent} from './task/add/task-add.component';
import {TaskEditComponent} from './task/edit/task-edit.component';
import {ActionListComponent} from './action/list/action-list.component';
import {ActionDetailsComponent} from './action/details/action-details.component';
import {ActionAddComponent} from './action/add/action-add.component';
import {ActionEditComponent} from './action/edit/action-edit.component';

export const routes: Routes = [
  { path: '', redirectTo: 'task/list', pathMatch: 'full'},
  { path: 'task/list', component: TaskListComponent },
  { path: 'task-details/:id', component: TaskDetailsComponent },
  { path: 'task-add', component: TaskAddComponent },
  { path: 'task-edit/:id', component: TaskEditComponent },

  { path: 'action/list', component: ActionListComponent},
  { path: 'action-details/:id', component: ActionDetailsComponent },
  { path: 'action-add/:task_id', component: ActionAddComponent },
  { path: 'action-edit/:id', component: ActionEditComponent }
];
