import {NgModule} from '@angular/core';
import {Route, RouterModule, Routes} from '@angular/router';
import {userGuard} from "./guards/auth.guard";
import {AppLayoutModule, AppLayoutComponent} from 'ts-layout';

const appRoutes: Route[] = [
    {
        path: '',
        component: AppLayoutComponent,
        children: [
            {
                path: '',
                canActivate: [userGuard],
                loadChildren: () => import('./views/admin/dashboard/dashboard.module').then(m => m.DashboardModule)
            },
            {
                path: 'admin',
                canActivate: [userGuard],
                loadChildren: () => import('./views/admin/admin.module').then(m => m.AdminModule)
            },
            {
                path: 'find-partner',
                canActivate: [userGuard],
                loadChildren: () => import('./views/find-partner/find-partner.module').then(m => m.FindPartnerModule)
            },
            {
                path: 'find-partner',
                canActivate: [userGuard],
                loadChildren: () => import('./views/find-partner/find-partner.module').then(m => m.FindPartnerModule)
            },
            {
                path: 'ticket-list',
                canActivate: [userGuard],
                loadChildren: () => import('./views/ticket-list/ticket-list.module').then(m => m.TicketListModule)
            },
            {
                path: 'api-info',
                canActivate: [userGuard],
                loadChildren: () => import('./views/api-info/api-info.module').then(m => m.ApiInfoModule)
            }
        ]
    },
    {
        path: 'auth',
        loadChildren: () => import('./views/auth/auth.module').then(m => m.AuthModule)
    },
    {
        path: 'e404',
        loadChildren: () => import('./views/auth/error/error.module').then(m => m.ErrorModule)
    },
    {
        path: '**',
        pathMatch: 'full', redirectTo: '/e404'
    }
];

@NgModule({
    imports: [
        RouterModule.forRoot(appRoutes, {
            bindToComponentInputs: true,
            enableTracing: false,
            scrollPositionRestoration: 'enabled',
            anchorScrolling: 'enabled',
            onSameUrlNavigation: 'reload'
        })
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}