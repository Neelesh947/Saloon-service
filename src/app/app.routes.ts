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
import { NavbarAdmin } from './pages/Admin/navbar-admin/navbar-admin';
import { SaloonListAdmin } from './pages/Admin/saloon-list-admin/saloon-list-admin';
import { EditSaloonByAdmin } from './pages/Admin/edit-saloon-by-admin/edit-saloon-by-admin';
import { AddSaloonByAdmin } from './pages/Admin/add-saloon-by-admin/add-saloon-by-admin';
import { ServiceListAdmin } from './pages/Admin/service-list-admin/service-list-admin';
import { AddServiceListAdmin } from './pages/Admin/add-service-list-admin/add-service-list-admin';
import { EditServiceListAdmin } from './pages/Admin/edit-service-list-admin/edit-service-list-admin';
import { StaffListAdmin } from './pages/Admin/staff-list-admin/staff-list-admin';
import { AddStaffByAdmin } from './pages/Admin/add-staff-by-admin/add-staff-by-admin';
import { EditEmployeeByAdmin } from './pages/Admin/edit-employee-by-admin/edit-employee-by-admin';
import { AdminAnalytics } from './pages/Admin/admin-analytics/admin-analytics';
import { AdminList } from './pages/Super_admin/admin-list/admin-list';
import { CreateAdmin } from './pages/Super_admin/create-admin/create-admin';
import { EditAdmin } from './pages/Super_admin/edit-admin/edit-admin';

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
            { path: 'serivce-management', component: ServiceManagementBySuperAdmin },
            { path: 'admin-list', component: AdminList },
            { path: 'add-admin', component: CreateAdmin },
            { path: 'edit-admin', component: EditAdmin }
        ]
    },
    {
        path: 'admin-dashboard', component: AdminDashboard,
        children: [
            { path: '', component: NavbarAdmin },
            { path: 'saloon-list-by-admin', component: SaloonListAdmin },
            { path: 'edit-saloon-by-admin', component: EditSaloonByAdmin },
            { path: 'add-saloon-by-admin', component: AddSaloonByAdmin },
            { path: 'serivce-list-by-admin', component: ServiceListAdmin },
            { path: 'add-service-list-by-admin', component: AddServiceListAdmin },
            { path: 'edit-service-list-by-admin', component: EditServiceListAdmin },
            { path: 'staf-list-by-admin', component: StaffListAdmin },
            { path: 'add-staff-by-admin', component: AddStaffByAdmin },
            { path: 'edit-staff-by-admin', component: EditEmployeeByAdmin },
            { path: 'admin-dashboard-analytics', component: AdminAnalytics }
        ]
    },
    { path: 'staff-dashboard', component: StaffDashboard },
    { path: 'user-dashboard', component: UserDashboard },
];
