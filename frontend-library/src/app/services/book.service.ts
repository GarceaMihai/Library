import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Book} from '../model/book';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  baseUrl = 'http://localhost:8080/book';
  bookDataStream: any;
  allBooksDataStream: any;

  constructor(private httpClient: HttpClient) {
    this.bookDataStream = new BehaviorSubject<any>(null);
    this.allBooksDataStream = new BehaviorSubject<any>(null);
  }

  getAllBooks(): Observable<Book[]> {
    return this.httpClient.get<Book[]>(`${this.baseUrl}`);
  }

  getBook(id: number): Observable<Book> {
    return this.httpClient.get<Book>(`${this.baseUrl}/${id}`);
  }

  getBookDataStream() {
    return this.bookDataStream;
  }

  getAllBooksDataStream() {
    return this.allBooksDataStream;
  }

  updateBookDataStream(book: any) {
    this.bookDataStream.next(book);
  }

  updateAllBooksDataStream(books: any) {
    this.allBooksDataStream.next(books);
  }

  filterBooks(selectedTitles: any, selectedAuthors: any, selectedLanguages: any, minPublishingYear: any, maxPublishingYear: any, minNrOfPages: any, maxNrOfPages: any): Observable<any> {
    return this.httpClient.post(this.baseUrl + '/filter',
      {selectedTitles, selectedAuthors, selectedLanguages, minPublishingYear, maxPublishingYear, minNrOfPages, maxNrOfPages});
  }

}
