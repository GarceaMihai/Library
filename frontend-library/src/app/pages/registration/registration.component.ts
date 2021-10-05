import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm: FormGroup;
  user: User = <User>{};
  registrationSuccessful: boolean = false;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
      this.initRegistrationForm();
  }

  register(): void {
    this.user.email = this.registrationForm.value.email;
    this.user.username = this.registrationForm.value.username;
    this.user.password = this.registrationForm.value.password;
    this.userService.register(this.user.email,this.user.username,this.user.password).subscribe(data => {
      this.registrationSuccessful = true;
      this.initRegistrationForm();
    }, error => {
      this.registrationSuccessful = false;
      this.registrationForm.markAllAsTouched();
      throw error;
    });
  }

  initRegistrationForm(): void {
    this.registrationForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
    });
  }

}
