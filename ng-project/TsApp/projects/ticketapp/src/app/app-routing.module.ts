import {NgModule} from '@angular/core';
import {Route, RouterModule} from '@angular/router';
import { AppLayoutComponent} from 'ts-ui/app-layout';
import {userGuard} from "./guards/auth.guard";

const appRoutes: Route[] = [
    {
        path: '',
        component: AppLayoutComponent,
        canActivate: [userGuard],
        children: [ 
            {
                path: 'ticket-list',
                loadChildren: () => import('./views/ticket-list/ticket-list.module').then(m => m.TicketListModule)
            },
            {
                path: 'dashboard',
                loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule)
            },
            {
                path: 'chanels',
                loadChildren: () => import('./views/chanel/chanel.module').then(m => m.ChanelModule)
            },
            {
                path: 'questions',
                loadChildren: () => import('./views/question/question.module').then(m => m.QuestionModule)
            },
            {
                path: 'softwares',
                loadChildren: () => import('./views/software/software.module').then(m => m.SoftwareModule)
            },
            {
                path: 'group-helps',
                loadChildren: () => import('./views/group-help/group-help.module').then(m => m.GroupHelpModule)
            },
            {
                path: 'templates',
                loadChildren: () => import('./views/template/template-form.module').then(m => m.TemplateFormModule)
            },
            {
                path: 'user-info',
                loadChildren: () => import('./views/user-info/user-info.module').then(m => m.UserInfoModule)
            },
            {
                path: 'table-info',
                loadChildren: () => import('./views/table-info/table-info.module').then(m => m.TableInfoModule)
            },
            {
                path: 'ticket-form',
                loadChildren: () => import('./views/ticket-form/ticket-form.module').then(m => m.TicketFormModule)
            },
            {
                path: 'ticket-log',
                loadChildren: () => import('./views/ticket-log/ticket-log.module').then(m => m.TicketLogModule)
            },            
            {
                path: 'find-partner',
                loadChildren: () => import('./views/find-partner/find-partner.module').then(m => m.FindPartnerModule)
            },

            {
                path: 'api-info',
                loadChildren: () => import('./views/api-info/api-info.module').then(m => m.ApiInfoModule)
            },
            {
                path: 'user-config',
                loadChildren: () => import('./views/user-config/user-config.module').then(m => m.UserConfigModule)
            },
            {
                path: '**', pathMatch: 'full',
                redirectTo: '/ticket-list'
            }
        ]
    },
    {
        path: 'signin',
        loadChildren: () => import('./views/signin/signin.module').then(m => m.SigninModule)
    },
    {
        path: 'signup',
        canActivate: [userGuard],
        loadChildren: () => import('./views/signup/signup.module').then(m => m.SignupModule)
    },
    {
        path: 'not-found',
        loadChildren: () => import('./views/error/error.module').then(m => m.ErrorModule)
    },
    {
        path: '**',
        pathMatch: 'full',
        redirectTo: '/ql'
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