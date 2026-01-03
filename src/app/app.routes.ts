import { Routes } from '@angular/router';
import { LoginPages } from './HomePage/login-pages/login-pages';
import { ForgetPassword } from './HomePage/forget-password/forget-password';
import { AdminDashboard } from './pages/Admin/admin-dashboard/admin-dashboard';
import { StaffDashboard } from './pages/Staff/staff-dashboard/staff-dashboard';
import { UserDashboard } from './pages/User/user-dashboard/user-dashboard';
import { SuperAdminRouterOutlet } from './pages/Super_admin/super-admin-router-outlet/super-admin-router-outlet';
import { Navbar } from './pages/Super_admin/navbar/navbar';
import { SaloonManagements } from './pages/Super_admin/saloon-managements/saloon-managements';
import { AnalyticsDashboardSuperAdmin } from './pages/Super_admin/analytics-dashboard-super-admin/analytics-dashboard-super-admin';

export const routes: Routes = [
    { path: '', component: LoginPages },
    { path: 'forget-password', component: ForgetPassword },
    {
        path: 'super-admin-dashboard', component: SuperAdminRouterOutlet,
        children: [
            { path: '', component: Navbar },
            { path: 'saloon-management-admin', component: SaloonManagements },
            { path: 'super-admin-analytics', component: AnalyticsDashboardSuperAdmin }
        ]
    },
    { path: 'admin-dashboard', component: AdminDashboard },
    { path: 'staff-dashboard', component: StaffDashboard },
    { path: 'user-dashboard', component: UserDashboard },
];
