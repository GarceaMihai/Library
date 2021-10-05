import { Component, OnInit } from '@angular/core';
import {BookRequestService} from '../../services/book-request.service';
import {UserService} from '../../services/user.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-book-request',
  templateUrl: './book-request.component.html',
  styleUrls: ['./book-request.component.css']
})
export class BookRequestComponent implements OnInit {

  principal: any;
  principalRequests: any = []
  requestForm: FormGroup;

  constructor(private bookRequestService: BookRequestService,
              private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getPrincipal().subscribe(res => {
      this.principal = res;
      this.bookRequestService.findByUser(this.principal.id).subscribe(res => {
        this.principalRequests = res;
      });
      this.initRequestForm();
      this.subscribeToRequestsDataStream();
    });
  }

  initRequestForm() {
    this.requestForm = new FormGroup({
      title: new FormControl(''),
      isbn13: new FormControl('', Validators.required)
    });
  }

  requestBook() {
    let title = this.requestForm.value.title;
    let isbn13 = this.requestForm.value.isbn13;
    this.bookRequestService.requestBook(title, isbn13, this.principal.id).subscribe(res => {
      this.bookRequestService.findByUser(this.principal.id).subscribe(res => {
        this.bookRequestService.updateRequestsDataStream(res);
      });
      this.initRequestForm();
    }, error => {
      this.requestForm.markAllAsTouched();
      throw error;
    });
  }

  deleteRequest(requestId: number) {
    this.bookRequestService.delete(requestId).subscribe(res => {
      this.bookRequestService.findByUser(this.principal.id).subscribe(res => {
        this.bookRequestService.updateRequestsDataStream(res);
      });
    });
  }

  subscribeToRequestsDataStream() {
    this.bookRequestService.getRequestsDataStream().subscribe(res => {
      this.principalRequests = res;
    });
  }

}
