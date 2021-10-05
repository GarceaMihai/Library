import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  usersUrl: string = 'http://localhost:8080/admin/user';
  booksUrl: string = 'http://localhost:8080/admin/book';
  rentalsUrl: string = 'http://localhost:8080/admin/rental';
  reviewsUrl: string = 'http://localhost:8080/admin/review';
  requestsUrl: string = 'http://localhost:8080/admin/book-request';
  userDataStream: any;
  bookDataStream: any;
  rentalDataStream: any;
  reviewDataStream: any;
  requestDataStream: any;

  constructor(private httpClient: HttpClient) {
    this.userDataStream=new BehaviorSubject<any>(null);
    this.bookDataStream=new BehaviorSubject<any>(null);
    this.rentalDataStream=new BehaviorSubject<any>(null);
    this.reviewDataStream=new BehaviorSubject<any>(null);
    this.requestDataStream=new BehaviorSubject<any>(null);
  }

  getUserDataStream(): any {
    return this.userDataStream;
  }

  getBookDataStream(): any {
    return this.bookDataStream;
  }

  getRentalDataStream(): any {
    return this.rentalDataStream;
  }

  getReviewDataStream(): any {
    return this.reviewDataStream;
  }

  getRequestDataStream(): any {
    return this.requestDataStream;
  }

  updateUserDataStream(users: any): void {
    this.userDataStream.next(users);
  }

  updateBookDataStream(books: any): void {
    this.bookDataStream.next(books);
  }

  updateRentalDataStream(rentals: any): void {
    this.rentalDataStream.next(rentals);
  }

  updateReviewDataStream(reviews: any): void {
    this.reviewDataStream.next(reviews);
  }

  updateRequestDataStream(requests: any): void {
    this.requestDataStream.next(requests);
  }

  getAllUsers(): Observable<any> {
    return this.httpClient.get(this.usersUrl);
  }

  getAllBooks(): Observable<any> {
    return this.httpClient.get(this.booksUrl);
  }

  getAllReviews(): Observable<any> {
    return this.httpClient.get(this.reviewsUrl);
  }

  getAllRequests(): Observable<any> {
    return this.httpClient.get(this.requestsUrl);
  }

  getAllUnsatisfiedRequestsGroupedAndOrdered(): Observable<any> {
    return this.httpClient.get(this.requestsUrl + '/unsatisfied-grouped');
  }

  addUser(email: string, username: string, password: string): Observable<any> {
    return this.httpClient.post(this.usersUrl, {email, username, password});
  }

  deleteUser(userId: number): Observable<any> {
    return this.httpClient.delete(this.usersUrl + '/' + userId);
  }

  addBook(title: any, author: any, language: any, published: any, nrOfPages: any, nrOfCopies: any, isbn13: any): Observable<any> {
    return this.httpClient.post(this.booksUrl, {title, author, language, published, nrOfPages, nrOfCopies, isbn13});
  }

  deleteBook(bookId: number): Observable<any> {
    return this.httpClient.delete(this.booksUrl + '/' + bookId);
  }

  getAllRentals(): Observable<any> {
    return this.httpClient.get(this.rentalsUrl);
  }

  deleteRental(rentalId: number): Observable<any> {
    return this.httpClient.delete(this.rentalsUrl + '/' + rentalId);
  }

  addRental(userId: number, bookId: number, address: string, phone: string, rent: boolean): Observable<any> {
    return this.httpClient.post(this.rentalsUrl, {userId, bookId, address, phone, rent});
  }

  addReview(userId: number, bookId: number, body: string): Observable<any> {
    return this.httpClient.post(this.reviewsUrl, {userId, bookId, body});
  }

  deleteReview(reviewId: number): Observable<any> {
    return this.httpClient.delete(this.reviewsUrl + '/' + reviewId);
  }

  addRequest(bookTitle: string, isbn13: string, userId: number): Observable<any> {
    return this.httpClient.post(this.requestsUrl, {bookTitle, isbn13, userId});
  }

  deleteRequest(requestId: number): Observable<any> {
    return this.httpClient.delete(this.requestsUrl + '/' + requestId);
  }

  deleteRequestsByIsbn13(isbn13: string): Observable<any> {
    return this.httpClient.delete(this.requestsUrl + '/unsatisfied-grouped/' + isbn13);
  }

  getReview(reviewId: number): Observable<any> {
    return this.httpClient.get(this.reviewsUrl + '/' + reviewId);
  }

  updateReview(reviewId: number, userId: number, bookId: number, body: string): Observable<any> {
    return this.httpClient.put(this.reviewsUrl + '/' + reviewId, {userId, bookId, body});
  }

  getBook(bookId: number): Observable<any> {
    return this.httpClient.get(this.booksUrl + '/' + bookId);
  }

  updateBook(bookId: any,title: any, author: any, language: any, published: any, nrOfPages: any, nrOfCopies: any, isbn13: any): Observable<any> {
    return this.httpClient.put<any>(this.booksUrl + '/' + bookId, {title, author, language, published, nrOfPages, nrOfCopies, isbn13});
  }

  getUser(userId: number): Observable<any> {
    return this.httpClient.get(this.usersUrl + '/' + userId);
  }

  updateUser(userId: number, email: string, username: string): Observable<any> {
    let params = new HttpParams().set('changePassword', 'false');
    return this.httpClient.put(this.usersUrl + '/' + userId, {email, username, password: 'does-not-matter'}, {params: params});
  }

  changeUserPassword(userId: number, password: string): Observable<any> {
    let params = new HttpParams().set('changePassword', 'true');
    return this.httpClient.put(this.usersUrl + '/' + userId, {email: 'does-not-matter', username: 'does-not-matter', password}, {params: params});
  }

  updateRental(rentalId: number, userId:number, bookId: number, address: string, phone: string): Observable<any> {
    return this.httpClient.put(this.rentalsUrl + '/' + rentalId, {userId, bookId, address, phone});
  }

  getRental(rentalId: number): Observable<any> {
    return this.httpClient.get(this.rentalsUrl + '/' + rentalId);
  }

  getRequest(reqeustId: number): Observable<any> {
    return this.httpClient.get(this.requestsUrl + '/' + reqeustId);
  }

  updateRequest(requestId: number, bookTitle: string, isbn13: string, userId: number): Observable<any> {
    return this.httpClient.put(this.requestsUrl + '/' + requestId, {bookTitle, isbn13, userId});
  }

  getNrOfActiveUsers(): Observable<any> {
    return this.httpClient.get(this.usersUrl + '/active');
  }

  exportAllUsers(fileType: string): Observable<any> {
    return this.httpClient.get(this.usersUrl + '/export', {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

  exportUser(userId: number, fileType: string): Observable<any> {
    return this.httpClient.get(this.usersUrl + '/export/' + userId, {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

  exportAllBooks(fileType: string): Observable<any> {
    return this.httpClient.get(this.booksUrl + '/export', {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

  exportBook(bookId: number, fileType: string): Observable<any> {
    return this.httpClient.get(this.booksUrl + '/export/' + bookId, {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

  exportAllRentals(fileType: string): Observable<any> {
    return this.httpClient.get(this.rentalsUrl + '/export', {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

  exportRental(rentalId: number, fileType: string): Observable<any> {
    return this.httpClient.get(this.rentalsUrl + '/export/' + rentalId, {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

  exportAllReviews(fileType: string): Observable<any> {
    return this.httpClient.get(this.reviewsUrl + '/export', {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

  exportReview(reviewId: number, fileType: string): Observable<any> {
    return this.httpClient.get(this.reviewsUrl + '/export/' + reviewId, {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

  exportAllRequests(fileType: string): Observable<any> {
    return this.httpClient.get(this.requestsUrl + '/export', {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

  exportRequest(requestId: number, fileType: string): Observable<any> {
    return this.httpClient.get(this.requestsUrl + '/export/' + requestId, {
      params: {fileType: fileType},
      responseType: 'text'
    });
  }

}
