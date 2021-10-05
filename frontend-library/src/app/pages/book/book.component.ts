import {Component, OnInit} from '@angular/core';
import {BookService} from '../../services/book.service';
import {Book} from '../../model/book';
import {ActivatedRoute} from '@angular/router';
import {RentalService} from '../../services/rental.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Rental} from '../../model/rental';
import {UserService} from '../../services/user.service';
import {ReviewService} from '../../services/review.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {

  bookId: number;
  book: Book;
  rentalForm: FormGroup;
  reviewForm: FormGroup
  recommendationForm: FormGroup;
  closeResult: string;
  rental: Rental = <Rental>{};
  principal: any;
  reviews: any = [];

  constructor(private bookService: BookService,
              private rentalService: RentalService,
              private userService: UserService,
              private reviewService: ReviewService,
              private route: ActivatedRoute,
              private modalService: NgbModal) { }

  ngOnInit(): void {
    this.bookId = this.route.snapshot.params['id'];
    this.bookService.getBook(this.bookId).subscribe((res) => {
        this.book = res;
        this.initRentalForm();
        this.initReviewForm();
        this.reviewService.findByBook(this.bookId).subscribe(res => {
          this.reviews = res;
        });
      },
      (error) => {

      });
    this.userService.getPrincipal().subscribe(res => {
      this.principal = res;
    });
    this.initRecommendationForm();
    this.subscribeToPrincipalDataStream();
    this.subscribeToBookDataStream();
    this.subscribeToReviewsDataStream();
  }

  rentOrReserve(): void {
    let rent: boolean =  this.book.nrOfCopies > this.book.nrOfActiveRentals ? true : false;
    this.rental.address = this.rentalForm.value.address;
    this.rental.phone = this.rentalForm.value.phone;
    this.rentalService.rent(this.principal.id, this.bookId,this.rental.address,this.rental.phone, rent)
      .subscribe(res => {
        this.book = res;
        this.userService.getPrincipal().subscribe(res => {
          this.userService.updatePrincipalDataStream(res);
        });
        this.bookService.getBook(this.bookId).subscribe(res => {
          this.bookService.updateBookDataStream(res);
        })
    }, error => {
      this.rentalForm.markAllAsTouched();
    });
  }

  addToWishlist(): void {
    this.userService.addBookToWishlist(this.principal.id,this.bookId)
      .subscribe(res => {
        this.userService.getPrincipal().subscribe(res => {
          this.principal = res;
        });
    }, error => {

    });
  }

  removeBookFromWishlist(): void {
    this.userService.removeBookFromWishlist(this.principal.id, this.bookId).subscribe(res => {
      this.principal = res;
    }, error => {

    });
  }

  initRentalForm(): void {
    this.rentalForm = new FormGroup({
      address: new FormControl('', Validators.required),
      phone: new FormControl('', Validators.required)
    });
  }

  initReviewForm(): void {
    this.reviewForm = new FormGroup({
      body: new FormControl('', Validators.required)
    });
  }

  initRecommendationForm(): void {
    this.recommendationForm = new FormGroup({
      recommendTo: new FormControl('', Validators.required)
    });
  }

  open(content) {
    this.initRentalForm();
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  checkIfBookIsWished(): boolean {
    for(let bookId of this.principal.wishedBooksIds){
      if(bookId == this.bookId){
        return true;
      }
    }
    return false;
  }

  checkIfBookIsRentedByPrincipal(): boolean {
    for(let bookId of this.principal.rentedBooksIds){
      if(bookId == this.bookId){
        return true;
      }
    }
    return false;
  }

  subscribeToPrincipalDataStream(): void {
    this.userService.getPrincipalDataStream().subscribe(res => {
      this.principal=res;
    })
  }

  subscribeToBookDataStream(): void {
    this.bookService.getBookDataStream().subscribe(res => {
      this.book=res;
    })
  }

  subscribeToReviewsDataStream(): void {
    this.reviewService.getReviewsDataStream().subscribe(res => {
      this.reviews = res;
    });
  }

  getNrOfAvailableCopies(): number {
    if(this.book.nrOfActiveRentals >= this.book.nrOfCopies){
      return 0;
    } else {
      return this.book.nrOfCopies - this.book.nrOfActiveRentals;
    }
  }

  postReview(): void {
    let body = this.reviewForm.value.body;
    this.reviewService.postReview(this.principal.id, this.bookId, body).subscribe(res => {
      this.reviewService.findByBook(this.bookId).subscribe(res => {
        this.reviewService.updateReviewsDataStream(res);
      });
      this.initReviewForm()
    }, error => {
      this.reviewForm.markAllAsTouched();
    });
  }

  deleteReview(reviewId: number): void {
    this.reviewService.delete(reviewId).subscribe(res => {
      this.reviewService.findByBook(this.bookId).subscribe(res => {
        this.reviewService.updateReviewsDataStream(res);
      });
    });
  }

  recommendBook() {
    let message = this.principal.username + ' recommended ' + this.book.title + ', by ' + this.book.author;
    let sendTo = this.recommendationForm.value.recommendTo;
    this.userService.recommendBook(message, sendTo).subscribe(res => {
      this.initRecommendationForm();
    }, error => {
      this.recommendationForm.markAllAsTouched();
      throw error;
    });
  }

}
