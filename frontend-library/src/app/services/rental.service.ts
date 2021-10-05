import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Book} from '../model/book';

@Injectable({
  providedIn: 'root'
})
export class RentalService {

  baseUrl = 'http://localhost:8080/rental';

  constructor(private httpClient: HttpClient) { }

  rent(userId: number, bookId: number, address: string, phone: string, rent: boolean): Observable<Book> {
    return this.httpClient.post<Book>(this.baseUrl, {userId, bookId, address, phone, rent});
  }

  findByUser(userId: number): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/' + userId);
  }

}
