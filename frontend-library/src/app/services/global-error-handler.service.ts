import {ErrorHandler, Injectable, NgZone} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class GlobalErrorHandlerService implements ErrorHandler {

  constructor(private snackBar: MatSnackBar, private zone: NgZone) { }

  handleError(error: any): void {
    const err = {
      message: error.message ? error.message : error.toString(),
      stack: error.stack ? error.stack : ''
    };
    let message = null;
    if(error.status == 500) {
      message = error.error.message;
      this.openSnackBar(message);
    } else if(error.status == 401) {
      message = "Invalid username or password";
      this.openSnackBar(message);
    }
  }

  openSnackBar(message: string): void {
    this.zone.run(() => {
      const snackBar = this.snackBar.open(message, "Close", {
        duration: 2000,
        verticalPosition: 'top'
      });
      snackBar.onAction().subscribe(() => {
        snackBar.dismiss();
      })
    });
  }

}
