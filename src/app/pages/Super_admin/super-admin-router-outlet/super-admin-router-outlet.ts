import { Component } from '@angular/core';
import { Navbar } from "../navbar/navbar";
import { RouterModule } from "@angular/router";

@Component({
  selector: 'app-super-admin-router-outlet',
  imports: [Navbar, RouterModule],
  templateUrl: './super-admin-router-outlet.html',
  styleUrl: './super-admin-router-outlet.scss',
})
export class SuperAdminRouterOutlet {

}
