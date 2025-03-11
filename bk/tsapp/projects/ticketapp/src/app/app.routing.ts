import { NgModule } from '@angular/core';
import { Route, RouterModule } from '@angular/router';

const appRoutes: Route[] = [
    {
        path: 'signin',
        loadChildren: () => import('./views/signin/signin.module').then(m => m.SigninModule)
    },
    {
        path: 'signup',
        loadChildren: () => import('./views/signup/signup.module').then(m => m.SignupModule)
    },
    {
        path: 'not-found',
        loadChildren: () => import('./views/error/error.module').then(m => m.ErrorModule)
    },
    {
        path: '',
        loadChildren: () => import('./admin.routing').then(m => m.AdminRouting)
    },
    {
        path: '**',
        pathMatch: 'full',
        redirectTo: 'not-found'
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
export class AppRouting {
}