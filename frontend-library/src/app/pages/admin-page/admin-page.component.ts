import { Component, OnInit } from '@angular/core';
import {AdminService} from '../../services/admin.service';
import {saveAs} from 'file-saver';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {

  option: number;
  nrOfActiveUsers: number;
  exportForm = new FormGroup({
    fileType: new FormControl('', Validators.required)
  });

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.adminService.getNrOfActiveUsers().subscribe(res => {
      this.nrOfActiveUsers = res;
    });
  }

  chooseBooks(): void {
    this.option = 0;
  }

  chooseUsers(): void {
    this.option = 1;
  }

  chooseRentals(): void {
    this.option = 2;
  }

  chooseReviews(): void {
    this.option = 3;
  }

  chooseRequests(): void {
    this.option = 4;
  }

  exportAll() {
    let fileType = this.exportForm.value.fileType;
    if(this.option == 1) {
      this.adminService.exportAllUsers(fileType).subscribe(res => {
        if(res) {
          let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
          let blob = new Blob([res],{type:typeForBlob});
          saveAs(blob,"all-users."+fileType);
        }
      });
    } else if(this.option == 0) {
      this.adminService.exportAllBooks(fileType).subscribe(res => {
        if(res) {
          let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
          let blob = new Blob([res],{type:typeForBlob});
          saveAs(blob,"all-books."+fileType);
        }
      });
    } else if(this.option == 2) {
      this.adminService.exportAllRentals(fileType).subscribe(res => {
        if(res) {
          let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
          let blob = new Blob([res],{type:typeForBlob});
          saveAs(blob,"all-rentals."+fileType);
        }
      });
    } else if(this.option == 3) {
      this.adminService.exportAllReviews(fileType).subscribe(res => {
        if(res) {
          let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
          let blob = new Blob([res],{type:typeForBlob});
          saveAs(blob,"all-reviews."+fileType);
        }
      });
    } else if(this.option == 4) {
      this.adminService.exportAllRequests(fileType).subscribe(res => {
        if(res) {
          let typeForBlob= fileType == 'txt' ? 'text/plain;charset=utf-8' : 'text/xml;charset=utf-8';
          let blob = new Blob([res],{type:typeForBlob});
          saveAs(blob,"all-requests."+fileType);
        }
      });
    }
  }

}
