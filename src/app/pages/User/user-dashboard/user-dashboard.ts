import { Component } from '@angular/core';
import { Router, RouterModule } from "@angular/router";
import { NavbarUser } from "../navbar-user/navbar-user";
import { FormsModule } from '@angular/forms';

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

  constructor(private router: Router) {}

  logout() {}
  openNotifications(){}
  onSearch()  {}
}
