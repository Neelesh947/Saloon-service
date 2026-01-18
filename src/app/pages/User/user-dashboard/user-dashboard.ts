import { Component } from '@angular/core';
import { Router, RouterModule } from "@angular/router";
import { NavbarUser } from "../navbar-user/navbar-user";
import { FormsModule } from '@angular/forms';
import { TokenStorageService } from '../../../services/auth-services/token-storage-services';
import { LoginServices } from '../../../services/auth-services/login-services';

@Component({
  selector: 'app-user-dashboard',
  imports: [RouterModule, FormsModule],
  templateUrl: './user-dashboard.html',
  styleUrl: './user-dashboard.scss',
})
export class UserDashboard {

  userName = 'John Doe';
  searchQuery = '';
  notifications = [
    { id: 1, message: 'Booking confirmed' },
    { id: 2, message: 'New offer available' },
    { id: 3, message: 'Booking reminder' }
  ];

  constructor(private router: Router, private login_service: LoginServices, private token_storgae: TokenStorageService) { }

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
  openNotifications() { }
  onSearch() { }
}
