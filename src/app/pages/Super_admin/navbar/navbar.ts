import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule,RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {

  isClosed: boolean = false;

  isSidebarOpen = true;
  isLoggedIn = true; // Set to true when user is logged in
  userName = 'Admin'; // Replace with dynamic user name

  toggleSidebar() {
    this.isClosed = !this.isClosed;
  }
}
