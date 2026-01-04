import { Component } from '@angular/core';
import { NavbarAdmin } from "../navbar-admin/navbar-admin";
import { RouterModule } from "@angular/router";

@Component({
  selector: 'app-admin-dashboard',
  imports: [NavbarAdmin, RouterModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss',
})
export class AdminDashboard {

}
