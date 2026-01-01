import { Component } from '@angular/core';

@Component({
  selector: 'app-forget-password',
  imports: [],
  templateUrl: './forget-password.html',
  styleUrl: './forget-password.scss',
})
export class ForgetPassword {
  step = 1;

  sendOtp() {
    this.step = 2;
  }

  verifyOtp() {
    this.step = 3;
  }

  resetPassword() {
    // call reset password API
  }
}
