import { Routes } from '@angular/router';
import { LoginPages } from './HomePage/login-pages/login-pages';
import { ForgetPassword } from './HomePage/forget-password/forget-password';
import { AdminDashboard } from './pages/Admin/admin-dashboard/admin-dashboard';
import { StaffDashboard } from './pages/Staff/staff-dashboard/staff-dashboard';
import { UserDashboard } from './pages/User/user-dashboard/user-dashboard';
import { SuperAdminRouterOutlet } from './pages/Super_admin/super-admin-router-outlet/super-admin-router-outlet';
import { Navbar } from './pages/Super_admin/navbar/navbar';
import { AnalyticsDashboardSuperAdmin } from './pages/Super_admin/analytics-dashboard-super-admin/analytics-dashboard-super-admin';
import { UserManagementBySuperAdmin } from './pages/Super_admin/user-management-by-super-admin/user-management-by-super-admin';
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
import { EditUsermanagementBySuperAdmin } from './pages/Super_admin/edit-usermanagement-by-super-admin/edit-usermanagement-by-super-admin';
import { NavbarUser } from './pages/User/navbar-user/navbar-user';
import { SaloonListComponent } from './pages/User/saloon-list-component/saloon-list-component';
import { ServiceListComponent } from './pages/User/service-list-component/service-list-component';
import { RegisterSaloon } from './HomePage/register-saloon/register-saloon';
import { CreateAccount } from './HomePage/create-account/create-account';
import { AboutUs } from './HomePage/about-us/about-us';

export const routes: Routes = [
    { path: '', component: LoginPages },
    { path: 'forget-password', component: ForgetPassword },
    { path: 'register-saloon', component: RegisterSaloon },
    { path: 'create-user', component: CreateAccount },
    { path: 'about-us', component: AboutUs },
    {
        path: 'super-admin-dashboard', component: SuperAdminRouterOutlet,
        children: [
            { path: '', component: Navbar },
            { path: 'super-admin-analytics', component: AnalyticsDashboardSuperAdmin },
            { path: 'user-management', component: UserManagementBySuperAdmin },
            { path: 'edit-user-management/:id', component: EditUsermanagementBySuperAdmin },
            { path: 'admin-list', component: AdminList },
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
    {
        path: 'user-dashboard', component: UserDashboard,
        children: [
            // { path: '', component: NavbarUser },
            { path: 'saloons', component: SaloonListComponent },
            { path: 'service', component: ServiceListComponent }
        ]
    },
];
