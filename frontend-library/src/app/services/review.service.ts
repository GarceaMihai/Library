import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  baseUrl = 'http://localhost:8080/review';
  reviewsDataStream: any;

  constructor(private httpClient: HttpClient) {
    this.reviewsDataStream=new BehaviorSubject<any>(null);
  }

  postReview(userId: number, bookId: number, body: string): Observable<any> {
    return this.httpClient.post(this.baseUrl, {userId, bookId, body});
  }

  findByBook(bookId: number): Observable<any> {
    return this.httpClient.get(this.baseUrl + '/' + bookId);
  }

  getReviewsDataStream() {
    return this.reviewsDataStream;
  }

  updateReviewsDataStream(reviews: any) {
    this.reviewsDataStream.next(reviews);
  }

  delete(reviewId: number): Observable<any> {
    return this.httpClient.delete(this.baseUrl + '/' + reviewId);
  }

}
