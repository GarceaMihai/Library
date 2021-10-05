import {ErrorHandler, HostListener, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './pages/login/login.component';
import { LibraryComponent } from './pages/library/library.component';
import { BookComponent } from './pages/book/book.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { RegistrationComponent } from './pages/registration/registration.component';
import {AuthenticationInterceptorService} from './services/authentication-interceptor.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ProfileComponent } from './pages/profile/profile.component';
import {MatListModule} from '@angular/material/list';
import { AdminPageComponent } from './pages/admin-page/admin-page.component';
import { AdminOptionsComponent } from './pages/admin-options/admin-options.component';
import {GlobalErrorHandlerService} from './services/global-error-handler.service';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { BookRequestComponent } from './pages/book-request/book-request.component';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatInputModule} from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import {NgxSliderModule} from '@angular-slider/ngx-slider';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import {AuthGuardService} from './services/auth-guard.service';
import {UserGuardService} from './services/user-guard.service';
import {AdminGuardService} from './services/admin-guard.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LibraryComponent,
    BookComponent,
    RegistrationComponent,
    ProfileComponent,
    AdminPageComponent,
    AdminOptionsComponent,
    BookRequestComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgbModule,
    MatListModule,
    MatSnackBarModule,
    MatButtonToggleModule,
    MatInputModule,
    MatAutocompleteModule,
    NgMultiSelectDropDownModule.forRoot(),
    FormsModule,
    NgxSliderModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthenticationInterceptorService,
      multi: true
    },
    {
      provide: ErrorHandler,
      useClass: GlobalErrorHandlerService
    },
    AuthGuardService,
    UserGuardService,
    AdminGuardService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
