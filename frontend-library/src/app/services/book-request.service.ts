import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookRequestService {

  baseUrl = 'http://localhost:8080/book-request';
  requestsDataStream: any;

  constructor(private httpClient: HttpClient) {
    this.requestsDataStream = new BehaviorSubject<any>(null);
  }

  requestBook(bookTitle: string, isbn13: string, userId: number): Observable<any> {
    return this.httpClient.post(this.baseUrl, {bookTitle, isbn13, userId});
  }

  findByUser(userId): Observable<any> {
    return this.httpClient.get(this.baseUrl + '/' + userId);
  }

  delete(requestId): Observable<any> {
    return this.httpClient.delete(this.baseUrl + '/' + requestId);
  }

  getRequestsDataStream() {
    return this.requestsDataStream;
  }

  updateRequestsDataStream(requests: any) {
    this.requestsDataStream.next(requests);
  }

}
