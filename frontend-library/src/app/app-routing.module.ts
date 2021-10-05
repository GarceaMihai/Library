import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LibraryComponent} from './pages/library/library.component';
import {BookComponent} from './pages/book/book.component';
import {LoginComponent} from './pages/login/login.component';
import {RegistrationComponent} from './pages/registration/registration.component';
import {ProfileComponent} from './pages/profile/profile.component';
import {AdminPageComponent} from './pages/admin-page/admin-page.component';
import {BookRequestComponent} from './pages/book-request/book-request.component';
import {AuthGuardService} from './services/auth-guard.service';
import {AdminGuardService} from './services/admin-guard.service';
import {UserGuardService} from './services/user-guard.service';
import {NotFoundComponent} from './pages/not-found/not-found.component';

const routes: Routes = [
  {path: 'library', component: LibraryComponent, canActivate: [AuthGuardService, UserGuardService]},
  {path: '', redirectTo: 'library', pathMatch: 'full'},
  {path: 'library/book/:id', component: BookComponent, canActivate: [AuthGuardService, UserGuardService]},
  {path: 'login', component: LoginComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService, UserGuardService]},
  {path: 'admin', component: AdminPageComponent, canActivate: [AuthGuardService, AdminGuardService]},
  {path: 'request', component: BookRequestComponent, canActivate: [AuthGuardService, UserGuardService]},
  {path: '404', component: NotFoundComponent},
  {path: '**', redirectTo: '404', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
