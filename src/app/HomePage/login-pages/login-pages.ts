import { Component } from '@angular/core';
import { LoginServices } from '../../services/auth-services/login-services';
import { Router } from '@angular/router';
import { LoginRequestDto } from '../../services/DTOs/login-request-dto';
import { LoginResponseDto } from '../../services/DTOs/login-response-dto';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TokenStorageService } from '../../services/auth-services/token-storage-services';

@Component({
  selector: 'app-login-pages',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login-pages.html',
  styleUrl: './login-pages.scss',
})
export class LoginPages {

  requestBody: LoginRequestDto = {
    username: '',
    password: ''
  }

  errorMsg: Array<String> = [];
  isLoading = false;

  constructor(private loginService: LoginServices, private router: Router, private tokenStorageService: TokenStorageService) { }

  login() {
    this.errorMsg = [];
    this.isLoading = true;

    if (!this.requestBody.username || !this.requestBody.password) {
      this.errorMsg.push('Username and password are required');
      return;
    }

    this.loginService.login(this.requestBody).subscribe({
      next: (response: LoginResponseDto) => {
        this.isLoading = false;
        //store the token
        this.tokenStorageService.storeLoginData(response);
        const roles = this.tokenStorageService.getRoles();
        const firstRole = roles.length != null ? roles[0] : '';
        switch (firstRole) {
          case 'SUPER_ADMIN':
            this.router.navigate(['/super-admin-dashboard']);
            break;
          case 'ADMIN':
            this.router.navigate(['/admin-dashboard']);
            break;
          case 'STAFF':
            this.router.navigate(['/staff-dashboard']);
            break;
          case 'USER':
            this.router.navigate(['/user-dashboard']);
            break;
          default:
            this.router.navigate(['']);
            sessionStorage.clear();
        }
      }, error: (err) => {
        this.isLoading = false;
        // this.errorMsg = 'Invalid username or password';
        console.error('Login error:', err);
      }
    })
  }
}
