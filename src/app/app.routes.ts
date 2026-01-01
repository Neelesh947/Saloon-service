import { Routes } from '@angular/router';
import { LoginPages } from './HomePage/login-pages/login-pages';
import { ForgetPassword } from './HomePage/forget-password/forget-password';
import { SuperAdminDashboard } from './pages/Super_admin/super-admin-dashboard/super-admin-dashboard';
import { AdminDashboard } from './pages/Admin/admin-dashboard/admin-dashboard';
import { StaffDashboard } from './pages/Staff/staff-dashboard/staff-dashboard';
import { UserDashboard } from './pages/User/user-dashboard/user-dashboard';

export const routes: Routes = [
    { path: '', component: LoginPages },
    { path: 'forget-password', component: ForgetPassword },
    { path: 'super-admin-dashboard', component: SuperAdminDashboard },
    { path: 'admin-dashboard', component: AdminDashboard },
    { path: 'staff-dashboard', component: StaffDashboard },
    { path: 'user-dashboard', component: UserDashboard },
];
