import {Component} from '@angular/core';
import {LoginService} from './services/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'frontend-library';

  constructor(private loginService: LoginService) {
  }

  logout(): void {
    this.loginService.logout().subscribe(res => {});
    sessionStorage.removeItem('authentication');
  }

  checkAuthenticated():boolean {
    if(sessionStorage.getItem('authentication') != null) {
      return true;
    }
    return false;
  }

  checkAuthorization(): boolean {
    if(sessionStorage.getItem('principalRole') === 'ROLE_USER') {
      return true;
    }
    return false;
  }

}
