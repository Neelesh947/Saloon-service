import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

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

  toggleSidebar() {
    this.isClosed = !this.isClosed;
  }

  logout() { }
}
