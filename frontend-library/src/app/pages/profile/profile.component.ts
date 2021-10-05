import { Component, OnInit } from '@angular/core';
import {RentalService} from '../../services/rental.service';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  principal: any;
  rentals: any;
  bookWishlist: any;

  constructor(private userService: UserService, private rentalService: RentalService) { }

  ngOnInit(): void {
    this.userService.getPrincipal().subscribe(res => {
      this.principal = res;
      this.rentalService.findByUser(this.principal.id).subscribe(res => {
        this.rentals = res;
      });
      this.userService.getBookWishlist(this.principal.id).subscribe(res => {
        this.bookWishlist = res;
      });
    });
  }

}
