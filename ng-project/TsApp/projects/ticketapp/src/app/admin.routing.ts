import { NgModule } from '@angular/core';
import { Route, RouterModule } from '@angular/router';
import { AppLayoutComponent } from 'ts-ui/app-layout';
import { userGuard } from "./guards/auth.guard";

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
                path: 'ghelps',
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
                path: 'ag-field',
                loadChildren: () => import('./views/ag-field/ag-field.module').then(m => m.AgFieldModule)
            },
            {
                path: 'react-to-angular',
                loadChildren: () => import('./views/react2ng/item-routing.module').then(m => m.Rect2NgRoutingModule)
            },
            {
                path: 'catalog',
                loadChildren: () => import('./views/catalog/catalog.module').then(m => m.CatalogModule)
            },
            {
                path: 'email-template',
                loadChildren: () => import('./views/email-template/email-template.module').then(m => m.EmailTemplateModule)
            },
            {
                path: '**',
                pathMatch: 'prefix',
                redirectTo: 'dashboard'
            }
        ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(appRoutes)
    ],
    exports: [RouterModule]
})
export class AdminRouting {
}