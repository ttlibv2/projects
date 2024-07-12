import {NgModule} from '@angular/core';
import {Route, RouterModule} from '@angular/router';
import {userGuard} from "./guards/auth.guard";
import { AppLayoutComponent} from 'ts-layout';

const appRoutes: Route[] = [
    {
        path: 'admin',
        component: AppLayoutComponent,
        children: [
            {
                path: '',
                canActivate: [userGuard],
                loadChildren: () => import('./views/admin/admin.module').then(m => m.AdminModule)
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
        pathMatch: 'full', redirectTo: '/admin'
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