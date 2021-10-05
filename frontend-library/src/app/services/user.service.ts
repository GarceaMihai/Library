import { Injectable } from '@angular/core';
import {HttpBackend, HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {User} from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl = 'http://localhost:8080/user';
  httpUnauthenticatedClient: HttpClient;
  principalDataStream:any;

  constructor(private httpClient: HttpClient, handler: HttpBackend) {
    this.httpUnauthenticatedClient = new HttpClient(handler);
    this.principalDataStream=new BehaviorSubject<any>(null);
  }

  getPrincipal(): Observable<any> {
    return this.httpClient.get<any>(`${this.baseUrl}`);
  }

  register(email: string, username: string, password: string): Observable<User> {
    return this.httpUnauthenticatedClient.post<User>(`${this.baseUrl}`, {email, username, password});
  }

  addBookToWishlist(userId: number, bookId: number): Observable<User> {
    return this.httpClient.post<User>(this.baseUrl + '/wishlist', {userId, bookId});
  }

  removeBookFromWishlist(userId: number, bookId: number): Observable<User> {
    return this.httpClient.delete<User>(this.baseUrl + '/wishlist' + '/' + userId + '/' + bookId);
  }

  getBookWishlist(userId: number): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/wishlist' + '/' + userId);
  }

  getPrincipalDataStream(){
    return this.principalDataStream;
  }

  updatePrincipalDataStream(principal:any){
    this.principalDataStream.next(principal);
  }

  getAllNonAdmins(): Observable<any> {
    return this.httpClient.get(this.baseUrl + '/role-user');
  }

  recommendBook(message: string, username: string): Observable<any> {
    return this.httpClient.post(this.baseUrl + '/recommend', {message}, {
        params: {username: username}
      });
  }

}
