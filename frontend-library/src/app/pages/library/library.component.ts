import {Component, OnDestroy, OnInit} from '@angular/core';
import {BookService} from '../../services/book.service';
import {Book} from '../../model/book';
import SockJS from 'sockjs-client';
import {UserService} from '../../services/user.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Stomp} from '@stomp/stompjs';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import {Options} from '@angular-slider/ngx-slider';

@Component({
  selector: 'app-library',
  templateUrl: './library.component.html',
  styleUrls: ['./library.component.css']
})
export class LibraryComponent implements OnInit, OnDestroy {

  books: Book[];
  stompClient: any;
  principal: any;
  closeResult: string;
  titlesDropdownList = [];
  selectedTitles = [];
  authorsDropdownList = [];
  selectedAuthors = [];
  languagesDropdownList = [];
  selectedLanguages = [];
  dropdownSettings: IDropdownSettings = {};
  yearSliderOptions: Options = {
    floor: -3000,
    ceil: new Date().getFullYear(),
    bindIndexForStepsArray: true
  };
  pagesSliderOptions: Options = {
    floor: 0,
    ceil: 25000,
    bindIndexForStepsArray: true
  };
  yearValue = this.yearSliderOptions.floor;
  yearHighValue = this.yearSliderOptions.ceil;
  pagesValue = this.pagesSliderOptions.floor;
  pagesHighValue = this.pagesSliderOptions.ceil;

  constructor(private bookService: BookService,
              private userService: UserService,
              private snackBar: MatSnackBar,
              private modalService: NgbModal) { }

  ngOnInit(): void {
    this.bookService.getAllBooks().subscribe((res) => {
        this.books = res;
        for(let i = 0; i < this.books.length; i++) {
          if(!this.titlesDropdownList.includes(this.books[i].title)) {
            this.titlesDropdownList.push(this.books[i].title);
          }
          if(!this.authorsDropdownList.includes(this.books[i].author)) {
            this.authorsDropdownList.push(this.books[i].author);
          }
          if(!this.languagesDropdownList.includes(this.books[i].language)) {
            this.languagesDropdownList.push(this.books[i].language);
          }
        }
        this.dropdownSettings = {
          singleSelection: false,
          idField: 'item_id',
          textField: 'item_text',
          selectAllText: 'Select All',
          unSelectAllText: 'UnSelect All',
          itemsShowLimit: 3,
          allowSearchFilter: true
        };
      },
      (_error) => {
        throw _error;
    });
    this.userService.getPrincipal().subscribe(res => {
      this.principal = res;
      this.subscribeToNotifications();
    }, error => {
      throw error;
    });
    this.subscribeToAllBooksDataStream();
  }

  subscribeToAllBooksDataStream() {
    this.bookService.getAllBooksDataStream().subscribe(res => {
      this.books = res;
    });
  }

  subscribeToNotifications() {
    const URL = "http://localhost:8080/socket";
    const websocket = new SockJS(URL);
    this.stompClient = Stomp.over(websocket);
    this.stompClient.connect({},()=>{
      this.stompClient.subscribe('/topic/socket/user/' + this.principal.id, notification => {
        let message = notification.body;
        this.snackBar.open(message,'Close',{
          duration:5000
        })
      })
    });
  }

  ngOnDestroy(): void {
    if(this.principal) {
      this.stompClient.disconnect();
    }
  }

  open(content) {
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

  applyFilter(): void {
    this.bookService.filterBooks(this.selectedTitles, this.selectedAuthors, this.selectedLanguages, this.yearValue, this.yearHighValue, this.pagesValue, this.pagesHighValue).subscribe(res => {
      this.bookService.updateAllBooksDataStream(res);
    }, error => {
      throw error;
    });
  }

  resetFilter(): void {
    this.selectedLanguages = [];
    this.selectedAuthors = [];
    this.selectedLanguages = [];
    this.yearValue = this.yearSliderOptions.floor;
    this.yearHighValue = this.yearSliderOptions.ceil;
    this.pagesValue = this.pagesSliderOptions.floor;
    this.pagesHighValue = this.pagesSliderOptions.ceil;
    this.bookService.getAllBooks().subscribe(res => {
      this.bookService.updateAllBooksDataStream(res);
    }, error => {
      throw error;
    });
  }

}
