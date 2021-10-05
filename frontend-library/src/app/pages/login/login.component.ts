import {Component, OnInit} from '@angular/core';
import {LoginService} from '../../services/login.service';
import {UserService} from '../../services/user.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  username: string;
  password: string;

  constructor(private loginService: LoginService,
              private userService: UserService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.initLoginForm();
  }

  login(): void {
    this.username = this.loginForm.value.username;
    this.password = this.loginForm.value.password;
    this.loginService.login(this.username, this.password).subscribe((res) => {
      sessionStorage.setItem('authentication', 'Basic ' + btoa(this.username + ':' + this.password));
      this.userService.getPrincipal().subscribe(res=> {
        if(res) {
          sessionStorage.setItem('principalRole', res.role);
          if (res.role == "ROLE_USER") {
            this.router.navigateByUrl('/library');
          } else {
            this.router.navigateByUrl('/admin');
          }
        }
      });
    }, error => {
      this.initLoginForm();
      throw error;
    });
  }

  initLoginForm(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });
  }

}
