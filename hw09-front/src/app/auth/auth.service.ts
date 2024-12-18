import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {LoginInfo} from './login-info';
import {RefreshInfo} from './refresh-info';

const apiURL = "http://localhost:9090";
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private httpClient: HttpClient) {}

  login(loginInfo: LoginInfo): Observable<any> {
    return this.httpClient.post<LoginInfo>(apiURL + '/login', JSON.stringify(loginInfo), this.httpOptions).pipe(
      catchError(this.handleError<LoginInfo>('login user'))
    )
  }

  refreshToken(refreshInfo: RefreshInfo): Observable<any> {
    return this.httpClient.post<RefreshInfo>(apiURL + '/oauth/access_token', JSON.stringify(refreshInfo), this.httpOptions).pipe(
      catchError(this.handleError<RefreshInfo>('refresh token'))
    )
  }

  handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
