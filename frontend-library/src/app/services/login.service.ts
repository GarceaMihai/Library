import {Injectable} from '@angular/core';
import {HttpBackend, HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  baseUrl = 'http://localhost:8080/login';
  httpUnauthenticatedClient: HttpClient;

  constructor(handler: HttpBackend, private httpClient: HttpClient) {
    this.httpUnauthenticatedClient = new HttpClient(handler);
  }

  login(username: string, password: string): Observable<any> {
     const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(username + ':' + password) });
     return this.httpUnauthenticatedClient.get(this.baseUrl, {headers: headers});
  }

  logout(): Observable<any> {
    return this.httpClient.get(this.baseUrl + '/logout');
  }

}
