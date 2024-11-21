import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import {Task} from '../task/task';
import {Action} from './action';

const apiURL = "http://localhost:9090/actions";

@Injectable({
  providedIn: 'root'
})
export class ActionService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  private pipe = new DatePipe('en-US');

  constructor(private httpClient: HttpClient) {}

  findAll(): Observable<any> {
    return this.httpClient.get<Action[]>(apiURL + '?', this.httpOptions).pipe(
      catchError(this.handleError<Action[]>('find all action', []))
    )
  }

  save(task_id: any, action: Action): Observable<any> {
    let task = new Task()
    task.id = task_id
    action.startDate = this.pipe.transform(action.startDate, 'yyyy-MM-dd HH:mm:ss')
    action.endDate = this.pipe.transform(action.endDate, 'yyyy-MM-dd HH:mm:ss')
    action.task = task
    return this.httpClient.post<Action>(apiURL, JSON.stringify(action), this.httpOptions).pipe(
      catchError(this.handleError<Action>('create action'))
    )
  }

  findById(id: any): Observable<any> {
    return this.httpClient.get<Action>(apiURL + '/' + id, this.httpOptions).pipe(
      catchError(this.handleError<Action>('find action by id'))
    )
  }

  updateById(id: any, action: Action): Observable<any> {
    action.startDate = this.pipe.transform(action.startDate, 'yyyy-MM-dd HH:mm:ss')
    action.endDate = this.pipe.transform(action.endDate, 'yyyy-MM-dd HH:mm:ss')
    return this.httpClient.put<Action>(apiURL + '/' + id, JSON.stringify(action), this.httpOptions).pipe(
      catchError(this.handleError<Action>('update action by id'))
    )
  }


  deleteById(id: any) {
    return this.httpClient.delete<Action>(apiURL + '/' + id, this.httpOptions).pipe(
      catchError(this.handleError<Action>('delete action by id'))
    )
  }

  handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
