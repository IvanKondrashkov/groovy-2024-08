import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import {Action} from './action';
import {User} from '../user/user';
import {Task} from '../task/task';

const apiURL = "http://localhost:9090/user/";

@Injectable({
  providedIn: 'root'
})
export class ActionService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('access_token')}`
    })
  };

  private pipe = new DatePipe('en-US');

  constructor(private httpClient: HttpClient) {}

  findAll(userId: any): Observable<any> {
    return this.httpClient.get<Action[]>(apiURL + userId + '/actions?', this.httpOptions).pipe(
      catchError(this.handleError<Action[]>('find all action', []))
    )
  }

  save(action: Action, userId: any, taskId: any): Observable<any> {
    action.task = this.mapToTask(taskId)
    action.initiator = this.mapToUser(userId)
    action.startDate = this.pipe.transform(action.startDate, 'yyyy-MM-dd HH:mm:ss')
    action.endDate = this.pipe.transform(action.endDate, 'yyyy-MM-dd HH:mm:ss')
    return this.httpClient.post<Action>(apiURL + userId + '/actions', JSON.stringify(action), this.httpOptions).pipe(
      catchError(this.handleError<Action>('create action'))
    )
  }

  findById(userId: any, id: any): Observable<any> {
    return this.httpClient.get<Action>(apiURL + userId + '/actions/' + id, this.httpOptions).pipe(
      catchError(this.handleError<Action>('find action by id'))
    )
  }

  updateById(userId: any, id: any, action: Action): Observable<any> {
    action.initiator = this.mapToUser(userId)
    action.startDate = this.pipe.transform(action.startDate, 'yyyy-MM-dd HH:mm:ss')
    action.endDate = this.pipe.transform(action.endDate, 'yyyy-MM-dd HH:mm:ss')
    return this.httpClient.put<Action>(apiURL + userId + '/actions/' + id, JSON.stringify(action), this.httpOptions).pipe(
      catchError(this.handleError<Action>('update action by id'))
    )
  }


  deleteById(userId: any, id: any) {
    return this.httpClient.delete<Action>(apiURL + userId + '/actions/' + id, this.httpOptions).pipe(
      catchError(this.handleError<Action>('delete action by id'))
    )
  }

  handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

  private mapToUser(userId: any) {
    let user = new User()
    user.id = userId
    return user
  }

  private mapToTask(taskId: any) {
    let task = new Task()
    task.id = taskId
    return task
  }
}
