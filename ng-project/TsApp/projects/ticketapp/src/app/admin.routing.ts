import { NgModule } from '@angular/core';
import { Route, RouterModule } from '@angular/router';
import { userGuard } from "./guards/auth.guard";
import { AppLayout } from './views/zlayout/app-layout';

const appRoutes: Route[] = [
    {
        path: '',
       //component: AppLayout,
        canActivate: [userGuard],
        children: [
            {
                path: 'ticket-list',
                loadChildren: () => import('./views/ticket-list/ticket-list.module').then(m => m.TicketListModule),
                data: { breadcrumb: '@@ticketlist' },
            },
            {
                path: 'ticket-form',
                loadChildren: () => import('./views/ticket-form/ticket-form.routing').then(m => m.TicketFormRouting),
                data: { breadcrumb: '@@router.ticketform' }
            },
            {
                path: 'ticket-log',
                loadChildren: () => import('./views/ticket-log/ticket-log.module').then(m => m.TicketLogModule),
                data: { breadcrumb: '@@ticketlog' }
            },
            {
                path: 'ticket-info/:ticket_id',
                loadChildren: () => import('./views/ticket-log/ticket-log.module').then(m => m.TicketLogModule),
                data: { breadcrumb: '@@ticketinfo' }
            },
            {
                path: 'dashboard',
                loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule),
                data: { breadcrumb: '@@dashboard' }
            },
            {
                path: 'chanels',
                loadChildren: () => import('./views/chanel/chanel.module').then(m => m.ChanelModule),
                data: { breadcrumb: '@@chanels' }
            },
            {
                path: 'chanel2s',
                loadChildren: () => import('./views/chanel2/chanel-routing.module').then(m => m.ChanelRoutingModule),
                data: { breadcrumb: '@@chanels' }
            },
            {
                path: 'question',
                loadChildren: () => import('./views/question/question.module').then(m => m.QuestionModule),
                data: { breadcrumb: '@@questions' }
            },
            {
                path: 'gsoft',
                loadChildren: () => import('./views/software/software.module').then(m => m.SoftwareModule),
                data: { breadcrumb: '@@softwares' }
            },
            {
                path: 'ghelp',
                loadChildren: () => import('./views/group-help/group-help.module').then(m => m.GroupHelpModule),
                data: { breadcrumb: '@@ghelps' }
            },
            {
                path: 'templates',
                loadChildren: () => import('./views/template/template-routing.module').then(m => m.TemplateRouting),
                data: { breadcrumb: '@@templates' }
            },
            {
                path: 'user-info',
                loadChildren: () => import('./views/user-info/user-info.module').then(m => m.UserInfoModule),
                data: { breadcrumb: '@@userinfo' }
            },
            {
                path: 'ag-table',
                loadChildren: () => import('./views/table-info/table-info.module').then(m => m.TableInfoModule),
                data: { breadcrumb: '@@agtable' }
            },

            {
                path: 'find-partner',
                loadChildren: () => import('./views/find-partner/find-partner.module').then(m => m.FindPartnerModule),
                data: { breadcrumb: '@@searchcustomer' }
            },

            {
                path: 'api-info',
                loadChildren: () => import('./views/api-info/api-info.module').then(m => m.ApiInfoModule),
                data: { breadcrumb: '@@apiinfo' }
            },
            {
                path: 'user-config',
                loadChildren: () => import('./views/user-config/user-config.module').then(m => m.UserConfigModule),
                data: { breadcrumb: '@@userconfig' }
            },
            {
                path: 'ag-field',
                loadChildren: () => import('./views/ag-field/ag-field.module').then(m => m.AgFieldModule),
                data: { breadcrumb: '@@agfield' }
            },
            {
                path: 'catalog',
                loadChildren: () => import('./views/catalog/catalog.module').then(m => m.CatalogModule),
                data: { breadcrumb: '@@catalog' }
            },
            {
                path: 'email-template',
                loadChildren: () => import('./views/email-template/email-template.module').then(m => m.EmailTemplateModule),
                data: { breadcrumb: '@@email-template' }
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