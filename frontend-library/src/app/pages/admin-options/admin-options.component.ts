import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {AdminService} from '../../services/admin.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {saveAs} from 'file-saver';

@Component({
  selector: 'app-admin-options',
  templateUrl: './admin-options.component.html',
  styleUrls: ['./admin-options.component.css']
})
export class AdminOptionsComponent implements OnInit, OnChanges {

  dataSource: any = [];
  countedBookRequests: any = [];
  addUserForm: FormGroup;
  addBookForm: FormGroup;
  addRentalForm: FormGroup;
  updateUserForm: FormGroup;
  changePasswordForm: FormGroup;
  reviewForm: FormGroup;
  requestForm: FormGroup;
  exportForm: FormGroup;
  editing: boolean;
  toUpdateId: number;
  closeResult: string;

  @Input()
  option: number;

  constructor(private adminService: AdminService, private modalService: NgbModal) { }

  ngOnInit(): void {
  }


  ngOnChanges(changes: SimpleChanges): void {
    this.dataSource=[];
    this.initExportForm();
    this.editing = false;
    this.toUpdateId = null;
    if(this.option == 0){
      this.initAddBookForm();
      this.adminService.getAllBooks().subscribe(res => {
        this.dataSource = res;
      });
      this.adminService.getBookDataStream().subscribe(res => {
        this.dataSource = res;
      });
    } else if(this.option == 1){
      this.initAddUserForm();
      this.adminService.getAllUsers().subscribe(res => {
        this.dataSource = res;
      });
      this.adminService.getUserDataStream().subscribe(res => {
        this.dataSource = res;
      });
    } else if(this.option == 2) {
      this.initAddRentalForm();
      this.adminService.getAllRentals().subscribe(res => {
        this.dataSource = res;
      });
      this.adminService.getRentalDataStream().subscribe(res => {
        this.dataSource = res;
      });
    } else if(this.option == 3) {
      this.initReviewForm();
      this.adminService.getAllReviews().subscribe(res => {
        this.dataSource = res;
      });
      this.adminService.getReviewDataStream().subscribe(res => {
        this.dataSource = res;
      });
    } else if(this.option == 4) {
      this.initRequestForm();
      this.adminService.getAllRequests().subscribe(res => {
        this.dataSource = res;
      });
      this.adminService.getRequestDataStream().subscribe(res => {
        this.dataSource = res;
      });
      this.adminService.getAllUnsatisfiedRequestsGroupedAndOrdered().subscribe(res => {
        this.countedBookRequests = res;
      });
    }
  }

  initAddUserForm(): void {
    this.addUserForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
    });
  }

  initAddBookForm(): void {
    this.addBookForm = new FormGroup({
      id: new FormControl({value: '', disabled: true}),
      title: new FormControl('', Validators.required),
      author: new FormControl('', Validators.required),
      language: new FormControl('', Validators.required),
      published: new FormControl('', [Validators.required, Validators.minLength(4)]),
      nrOfPages: new FormControl('', Validators.required),
      nrOfCopies: new FormControl('', Validators.required),
      isbn13: new FormControl('', Validators.required)
    });
  }

  initAddRentalForm(): void {
    this.addRentalForm = new FormGroup({
      userId: new FormControl('', Validators.required),
      bookId: new FormControl('', Validators.required),
      address: new FormControl('', Validators.required),
      phone: new FormControl('', Validators.required)
    });
  }

  initReviewForm() {
    this.reviewForm = new FormGroup({
      userId: new FormControl('', Validators.required),
      bookId: new FormControl('', Validators.required),
      body: new FormControl('', Validators.required)
    });
  }

  initRequestForm() {
    this.requestForm = new FormGroup({
      bookTitle: new FormControl(''),
      isbn13: new FormControl('', Validators.required),
      userId: new FormControl('', Validators.required)
    });
  }

  initExportForm() {
    this.exportForm = new FormGroup({
      fileType: new FormControl('', Validators.required)
    });
  }

  addUser(): void {
    let email = this.addUserForm.value.email;
    let username = this.addUserForm.value.username;
    let password = this.addUserForm.value.password;
    this.adminService.addUser(email,username,password).subscribe(data => {
      this.adminService.getAllUsers().subscribe(res => {
        this.adminService.updateUserDataStream(res);
      });
      this.initAddUserForm();
    }, error => {
      this.addUserForm.markAllAsTouched();
      throw error;
    });
  }

  addBook(): void {
    let title = this.addBookForm.value.title;
    let author = this.addBookForm.value.author;
    let language = this.addBookForm.value.language;
    let published = this.addBookForm.value.published;
    let nrOfPages = this.addBookForm.value.nrOfPages;
    let nrOfCopies = this.addBookForm.value.nrOfCopies;
    let isbn13 = this.addBookForm.value.isbn13;
    if(!this.editing) {
      this.adminService.addBook(title, author, language, published, nrOfPages, nrOfCopies, isbn13).subscribe(data => {
        this.adminService.getAllBooks().subscribe(res => {
          this.adminService.updateBookDataStream(res);
        });
        this.initAddBookForm();
      }, error => {
        this.addBookForm.markAllAsTouched();
        throw error;
      });
    } else {
      this.adminService.updateBook(this.toUpdateId,title, author, language, published, nrOfPages, nrOfCopies, isbn13).subscribe(data => {
        this.adminService.getAllBooks().subscribe(res => {
          this.adminService.updateBookDataStream(res);
        });
        this.initAddBookForm();
        this.editing = false;
        this.toUpdateId = null;
      }, error => {
        this.addBookForm.markAllAsTouched();
        throw error;
      });
    }
  }

  addRental(): void {
    let userId = this.addRentalForm.value.userId;
    let bookId = this.addRentalForm.value.bookId;
    let address = this.addRentalForm.value.address;
    let phone = this.addRentalForm.value.phone;
    if(!this.editing) {
      this.adminService.addRental(userId, bookId, address, phone, true).subscribe(data => {
        this.adminService.getAllRentals().subscribe(res => {
          this.adminService.updateRentalDataStream(res);
        });
        this.initAddRentalForm();
      }, error => {
        this.addRentalForm.markAllAsTouched();
        throw error;
      });
    } else {
      this.adminService.updateRental(this.toUpdateId, userId, bookId, address, phone).subscribe(res => {
        this.adminService.getAllRentals().subscribe(res => {
          this.adminService.updateRentalDataStream(res);
        });
        this.initAddRentalForm();
        this.editing = false;
        this.toUpdateId = null;
      }, error => {
        this.addRentalForm.markAllAsTouched();
        throw error;
      });
    }
  }

  addReview(): void {
    let userId = this.reviewForm.value.userId;
    let bookId = this.reviewForm.value.bookId;
    let body = this.reviewForm.value.body;
    if(!this.editing) {
      this.adminService.addReview(userId, bookId, body).subscribe(res => {
        this.adminService.getAllReviews().subscribe(res => {
          this.adminService.updateReviewDataStream(res);
        });
        this.initReviewForm();
      }, error => {
        this.reviewForm.markAllAsTouched();
        throw error;
      });
    } else {
      this.adminService.updateReview(this.toUpdateId, userId, bookId, body).subscribe(res => {
        this.adminService.getAllReviews().subscribe(res => {
          this.adminService.updateReviewDataStream(res);
        });
        this.initReviewForm();
        this.editing = false;
        this.toUpdateId = null;
      }, error => {
        this.reviewForm.markAllAsTouched();
        throw error;
      });
    }
  }

  addBookRequest() {
    let bookTitle = this.requestForm.value.bookTitle;
    let isbn13 = this.requestForm.value.isbn13;
    let userId = this.requestForm.value.userId;
    if(!this.editing) {
      this.adminService.addRequest(bookTitle, isbn13, userId).subscribe(res => {
        this.adminService.getAllRequests().subscribe(res => {
          this.adminService.updateRequestDataStream(res);
        });
        this.adminService.getAllUnsatisfiedRequestsGroupedAndOrdered().subscribe(res => {
          this.countedBookRequests = res;
        });
        this.initRequestForm();
      }, error => {
        this.requestForm.markAllAsTouched();
        throw error;
      });
    } else {
      this.adminService.updateRequest(this.toUpdateId, bookTitle, isbn13, userId, ).subscribe(res => {
        this.adminService.getAllRequests().subscribe(res => {
          this.adminService.updateRequestDataStream(res);
        });
        this.adminService.getAllUnsatisfiedRequestsGroupedAndOrdered().subscribe(res => {
          this.countedBookRequests = res;
        });
        this.initRequestForm();
        this.editing = false;
        this.toUpdateId = null;
      }, error => {
        this.requestForm.markAllAsTouched();
        throw error;
      });
    }
  }

  deleteUser(userId: number): void {
    this.adminService.deleteUser(userId).subscribe(data => {
      this.adminService.getAllUsers().subscribe(res => {
        this.adminService.updateUserDataStream(res);
      }, error => {
        throw error;
      });
    }, error => {
      throw error;
    });
  }

  deleteBook(bookId: number): void {
    this.adminService.deleteBook(bookId).subscribe(data => {
      this.adminService.getAllBooks().subscribe(res => {
        this.adminService.updateBookDataStream(res);
      });
    }, error => {
      throw error;
    });
  }

  deleteRental(rentalId: number): void {
    this.adminService.deleteRental(rentalId).subscribe(data => {
      this.adminService.getAllRentals().subscribe(res => {
        this.adminService.updateRentalDataStream(res);
      });
    }, error => {
      throw error;
    });
  }

  deleteReview(reviewId: number): void {
    this.adminService.deleteReview(reviewId).subscribe(res => {
      this.adminService.getAllReviews().subscribe(res => {
        this.adminService.updateReviewDataStream(res);
      });
    }, error => {
      throw error;
    });
  }

  deleteRequest(requestId: number): void {
    this.adminService.deleteRequest(requestId).subscribe(res => {
      this.adminService.getAllRequests().subscribe(res => {
        this.adminService.updateRequestDataStream(res);
      });
      this.adminService.getAllUnsatisfiedRequestsGroupedAndOrdered().subscribe(res => {
        this.countedBookRequests = res;
      });
    }, error => {
      throw error;
    });
  }

  viewBook(bookId: number) {
    this.adminService.getBook(bookId).subscribe(res => {
      this.addBookForm.get("id").setValue(res.id);
      this.addBookForm.get("title").setValue(res.title);
      this.addBookForm.get("author").setValue(res.author);
      this.addBookForm.get("language").setValue(res.language);
      this.addBookForm.get("published").setValue(res.published);
      this.addBookForm.get("nrOfPages").setValue(res.nrOfPages);
      this.addBookForm.get("nrOfCopies").setValue(res.nrOfCopies);
      this.addBookForm.get("isbn13").setValue(res.isbn13);
      this.editing = true;
      this.toUpdateId = bookId;
    })
  }

  cancelEdit(){
    this.editing = false;
    this.toUpdateId = null;
    this.initAddUserForm();
    this.initAddBookForm();
    this.initAddRentalForm();
    this.initReviewForm();
    this.initRequestForm();
    this.initExportForm();
  }

  open(content, userId) {
    this.viewUser(userId);
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

  viewUser(userId: number): void {
    this.adminService.getUser(userId).subscribe(res => {
      this.updateUserForm = new FormGroup({
        id: new FormControl({value: userId, disabled: true}),
        email: new FormControl(res.email, [Validators.required, Validators.email]),
        username: new FormControl(res.username, Validators.required)
      });
      this.changePasswordForm = new FormGroup({
        password: new FormControl('', Validators.required)
      });
      this.toUpdateId = userId;
    });
  }

  viewReview(reviewId: number): void {
    this.adminService.getReview(reviewId).subscribe(res => {
      this.reviewForm.get("userId").setValue(res.userId);
      this.reviewForm.get("bookId").setValue(res.bookId);
      this.reviewForm.get("body").setValue(res.body);
      this.editing = true;
      this.toUpdateId = reviewId;
    })
  }

  viewRental(rentalId: number): void {
    this.adminService.getRental(rentalId).subscribe(res => {
      this.addRentalForm.get("userId").setValue(res.userId);
      this.addRentalForm.get("bookId").setValue(res.bookId)
      this.addRentalForm.get("address").setValue(res.address);
      this.addRentalForm.get("phone").setValue(res.phone);
      this.editing = true;
      this.toUpdateId = rentalId;
    });
  }

  viewRequest(requestId: number): void {
    this.adminService.getRequest(requestId).subscribe(res => {
      this.requestForm.get("bookTitle").setValue(res.bookTitle);
      this.requestForm.get("isbn13").setValue(res.isbn13);
      this.requestForm.get("userId").setValue(res.userId);
      this.editing = true;
      this.toUpdateId = requestId;
    }, error => {
      throw error;
    });
  }

  updateUser() {
    let email = this.updateUserForm.value.email;
    let username = this.updateUserForm.value.username;
    this.adminService.updateUser(this.toUpdateId, email, username).subscribe(res => {
      this.adminService.getAllUsers().subscribe(res => {
        this.adminService.updateUserDataStream(res);
      });
    }, error => {
      throw error;
    });
    this.toUpdateId = null;
  }

  changePassword() {
    let password = this.changePasswordForm.value.password;
    this.adminService.changeUserPassword(this.toUpdateId, password).subscribe(res => {
      this.adminService.getAllUsers().subscribe(res => {
        this.adminService.updateUserDataStream(res);
      });
    }, error => {
      throw error;
    });
    this.toUpdateId = null;
  }

  exportUser() {
    let fileType = this.exportForm.value.fileType;
    this.adminService.exportUser(this.toUpdateId, fileType).subscribe(res => {
      if(res) {
        let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
        let blob = new Blob([res],{type:typeForBlob});
        saveAs(blob,"user-" + this.toUpdateId + '.' + fileType);
      }
    });
  }

  exportBook() {
    let fileType = this.exportForm.value.fileType;
    this.adminService.exportBook(this.toUpdateId, fileType).subscribe(res => {
      if(res) {
        let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
        let blob = new Blob([res],{type:typeForBlob});
        saveAs(blob,"book-" + this.toUpdateId + '.' + fileType);
      }
    });
  }

  exportRental() {
    let fileType = this.exportForm.value.fileType;
    this.adminService.exportRental(this.toUpdateId, fileType).subscribe(res => {
      if(res) {
        let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
        let blob = new Blob([res],{type:typeForBlob});
        saveAs(blob,"rental-" + this.toUpdateId + '.' + fileType);
      }
    });
  }

  exportReview() {
    let fileType = this.exportForm.value.fileType;
    this.adminService.exportReview(this.toUpdateId, fileType).subscribe(res => {
      if(res) {
        let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
        let blob = new Blob([res],{type:typeForBlob});
        saveAs(blob,"review-" + this.toUpdateId + '.' + fileType);
      }
    });
  }

  exportRequest() {
    let fileType = this.exportForm.value.fileType;
    this.adminService.exportRequest(this.toUpdateId, fileType).subscribe(res => {
      if(res) {
        let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
        let blob = new Blob([res],{type:typeForBlob});
        saveAs(blob,"request-" + this.toUpdateId + '.' + fileType);
      }
    });
  }

  deleteBookRequestsByIsbn13(isbn13: string) {
    this.adminService.deleteRequestsByIsbn13(isbn13).subscribe(res => {
      this.adminService.getAllUnsatisfiedRequestsGroupedAndOrdered().subscribe(res => {
        this.countedBookRequests = res;
      });
      this.adminService.getAllRequests().subscribe(res => {
        this.adminService.updateRequestDataStream(res);
      });
    }, error => {
      throw error;
    });
  }

}
