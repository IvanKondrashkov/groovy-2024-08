import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import {Task} from './task';

const apiURL = "http://localhost:9090/tasks";

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  private pipe = new DatePipe('en-US');

  constructor(private httpClient: HttpClient) {}

  findAll(): Observable<any> {
    return this.httpClient.get<Task[]>(apiURL + '?', this.httpOptions).pipe(
      catchError(this.handleError<Task[]>('find all task', []))
    )
  }

  save(task: Task): Observable<any> {
    task.startDate = this.pipe.transform(task.startDate, 'yyyy-MM-dd HH:mm:ss')
    task.endDate = this.pipe.transform(task.endDate, 'yyyy-MM-dd HH:mm:ss')
    return this.httpClient.post<Task>(apiURL, JSON.stringify(task), this.httpOptions).pipe(
      catchError(this.handleError<Task>('create task'))
    )
  }

  findById(id: any): Observable<any> {
    return this.httpClient.get<Task>(apiURL + '/' + id, this.httpOptions).pipe(
      catchError(this.handleError<Task>('find task by id'))
    )
  }

  updateById(id: any, task: Task): Observable<any> {
    task.startDate = this.pipe.transform(task.startDate, 'yyyy-MM-dd HH:mm:ss')
    task.endDate = this.pipe.transform(task.endDate, 'yyyy-MM-dd HH:mm:ss')
    return this.httpClient.put<Task>(apiURL + '/' + id, JSON.stringify(task), this.httpOptions).pipe(
      catchError(this.handleError<Task>('update task by id'))
    )
  }


  deleteById(id: any) {
    return this.httpClient.delete<Task>(apiURL + '/' + id, this.httpOptions).pipe(
      catchError(this.handleError<Task>('delete task by id'))
    )
  }

  handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
