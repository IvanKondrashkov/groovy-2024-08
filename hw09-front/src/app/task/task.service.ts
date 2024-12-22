import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import {Task} from './task';
import {User} from '../user/user';

const apiURL = "http://localhost:9090/user/";

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('access_token')}`
    })
  };

  private pipe = new DatePipe('en-US');

  constructor(private httpClient: HttpClient) {}

  findAll(userId: any): Observable<any> {
    return this.httpClient.get<Task[]>(apiURL + userId + '/tasks?', this.httpOptions).pipe(
      catchError(this.handleError<Task[]>('find all task', []))
    )
  }

  save(task: Task, userId: any): Observable<any> {
    task.initiator = this.mapToUser(userId)
    task.startDate = this.pipe.transform(task.startDate, 'yyyy-MM-dd HH:mm:ss')
    task.endDate = this.pipe.transform(task.endDate, 'yyyy-MM-dd HH:mm:ss')
    return this.httpClient.post<Task>(apiURL + userId + '/tasks', JSON.stringify(task), this.httpOptions).pipe(
      catchError(this.handleError<Task>('create task'))
    )
  }

  findById(userId: any, id: any): Observable<any> {
    return this.httpClient.get<Task>(apiURL + userId + '/tasks/' + id, this.httpOptions).pipe(
      catchError(this.handleError<Task>('find task by id'))
    )
  }

  updateById(userId: any, id: any, task: Task): Observable<any> {
    task.initiator = this.mapToUser(userId)
    task.startDate = this.pipe.transform(task.startDate, 'yyyy-MM-dd HH:mm:ss')
    task.endDate = this.pipe.transform(task.endDate, 'yyyy-MM-dd HH:mm:ss')
    return this.httpClient.put<Task>(apiURL + userId + '/tasks/' + id, JSON.stringify(task), this.httpOptions).pipe(
      catchError(this.handleError<Task>('update task by id'))
    )
  }


  deleteById(userId: any, id: any) {
    return this.httpClient.delete<Task>(apiURL + userId + '/tasks/' + id, this.httpOptions).pipe(
      catchError(this.handleError<Task>('delete task by id'))
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
}
