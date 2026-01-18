import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TokenStorageService } from '../../../services/auth-services/token-storage-services';
import { LoginServices } from '../../../services/auth-services/login-services';

@Component({
  selector: 'app-navbar-admin',
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar-admin.html',
  styleUrl: './navbar-admin.scss',
})
export class NavbarAdmin {
  isClosed: boolean = false;

  isSidebarOpen = true;
  isLoggedIn = true; // Set to true when user is logged in
  userName = 'Admin'; // Replace with dynamic user name

  constructor(private login_service: LoginServices, private token_storgae: TokenStorageService, private router: Router) { }

  toggleSidebar() {
    this.isClosed = !this.isClosed;
  }

  logout() {
    const realm = 'Saloon';
    const refreshToken = this.token_storgae.getRefreshToken();

    if (!refreshToken) {
      this.token_storgae.clear();
      this.router.navigate(['/']);
      return;
    }
    this.login_service.logout(realm).subscribe({
      next: () => {
        this.token_storgae.clear();
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Logout failed', err);
        this.token_storgae.clear();
        this.router.navigate(['/']);
      }
    });
  }
}
