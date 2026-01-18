import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar-user',
  imports: [FormsModule, RouterModule],
  templateUrl: './navbar-user.html',
  styleUrl: './navbar-user.scss',
})
export class NavbarUser {

  userName = 'John Doe';
  searchQuery = '';
  notifications = [
    { id: 1, message: 'Booking confirmed' },
    { id: 2, message: 'New offer available' },
    { id: 3, message: 'Booking reminder' }
  ];

  constructor(private router: Router) {}

  logout() {}
  openNotifications(){}
  onSearch()  {}
}
