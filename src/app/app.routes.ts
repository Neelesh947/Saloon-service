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
import { UserManagementBySuperAdmin } from './pages/Super_admin/user-management-by-super-admin/user-management-by-super-admin';
import { StaffmanagementBySuperAdmin } from './pages/Super_admin/staffmanagement-by-super-admin/staffmanagement-by-super-admin';
import { ServiceManagementBySuperAdmin } from './pages/Super_admin/service-management-by-super-admin/service-management-by-super-admin';
import { EditSaloonSuperAdmin } from './pages/Super_admin/edit-saloon-super-admin/edit-saloon-super-admin';
import { AddSaloonSuperAdmin } from './pages/Super_admin/add-saloon-super-admin/add-saloon-super-admin';

export const routes: Routes = [
    { path: '', component: LoginPages },
    { path: 'forget-password', component: ForgetPassword },
    {
        path: 'super-admin-dashboard', component: SuperAdminRouterOutlet,
        children: [
            { path: '', component: Navbar },
            { path: 'saloon-management-admin', component: SaloonManagements },
            { path: 'add-saloon-management-admin', component: AddSaloonSuperAdmin },
            { path: 'edit-saloon-management-admin/:id', component: EditSaloonSuperAdmin },
            { path: 'super-admin-analytics', component: AnalyticsDashboardSuperAdmin },
            { path: 'user-management', component: UserManagementBySuperAdmin },
            { path: 'staff-management', component: StaffmanagementBySuperAdmin },
            { path: 'serivce-management', component: ServiceManagementBySuperAdmin }
        ]
    },
    { path: 'admin-dashboard', component: AdminDashboard },
    { path: 'staff-dashboard', component: StaffDashboard },
    { path: 'user-dashboard', component: UserDashboard },
];
