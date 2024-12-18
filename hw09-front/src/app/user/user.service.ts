import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {User} from './user';

const apiURL = "http://localhost:9090/users";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('access_token')}`
    })
  };

  constructor(private httpClient: HttpClient) {}

  findAll(): Observable<any> {
    return this.httpClient.get<User[]>(apiURL + '/admin?', this.httpOptions).pipe(
      catchError(this.handleError<User[]>('find all user', []))
    )
  }

  save(task: User): Observable<any> {
    return this.httpClient.post<User>(apiURL + '/register', JSON.stringify(task), this.httpOptions).pipe(
      catchError(this.handleError<User>('create user'))
    )
  }

  findById(id: any): Observable<any> {
    return this.httpClient.get<User>(apiURL + '/admin/' + id, this.httpOptions).pipe(
      catchError(this.handleError<User>('find user by id'))
    )
  }

  updateById(id: any, task: User): Observable<any> {
    return this.httpClient.put<User>(apiURL + '/admin/' + id, JSON.stringify(task), this.httpOptions).pipe(
      catchError(this.handleError<User>('update user by id'))
    )
  }


  deleteById(id: any) {
    return this.httpClient.delete<User>(apiURL + '/admin/' + id, this.httpOptions).pipe(
      catchError(this.handleError<User>('delete user by id'))
    )
  }

  handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
