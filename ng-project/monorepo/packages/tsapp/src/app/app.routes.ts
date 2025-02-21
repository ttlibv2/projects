import { Routes } from '@angular/router';
import { DashboardPage } from './pages/dashboard/dashboard.page';

export const routes: Routes = [
    {
        path: 'dashboard', 
        title: 'Hệ thống báo cáo',
        component: DashboardPage,
    },
    {
        path: "**",
        redirectTo: "/dashboard",
        pathMatch: 'full'
    }
];