import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { userGuard } from "../../guards/auth.guard";

const routes: Routes = [
  {
    path: 'dashboard',
    //canActivate: [userGuard],
    loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  {
    path: 'chanels',
    // canActivate: [userGuard], 
    loadChildren: () => import('./chanel/chanel.module').then(m => m.ChanelModule)
  },
  {
    path: 'questions',
    //canActivate: [userGuard], 
    loadChildren: () => import('./question/question.module').then(m => m.QuestionModule)
  },
  {
    path: 'softwares',
    //canActivate: [userGuard], 
    loadChildren: () => import('./software/software.module').then(m => m.SoftwareModule)
  },
  {
    path: 'group-helps',
    //  canActivate: [userGuard],
    loadChildren: () => import('./group-help/group-help.module').then(m => m.GroupHelpModule)
  },
  {
    path: 'templates',
    //canActivate: [userGuard], 
    loadChildren: () => import('./template/template-form.module').then(m => m.TemplateFormModule)
  },
  {
    path: 'user-info',
    // canActivate: [userGuard],
    loadChildren: () => import('./user-info/user-info.module').then(m => m.UserInfoModule)
  },
  {
    path: 'table-info',
    // canActivate: [userGuard],
    loadChildren: () => import('./table-info/table-info.module').then(m => m.TableInfoModule)
  },
  {
    path: 'ticket-form',
    loadChildren: () => import('../ticket-form/ticket-form.module').then(m => m.TicketFormModule)
  },
  {
    path: 'ticket-log',
    //  canActivate: [userGuard],
    loadChildren: () => import('../ticket-log/ticket-log.module').then(m => m.TicketLogModule)
  },
  {
    path: 'ticket-list',
    // canActivate: [userGuard],
    loadChildren: () => import('../ticket-list/ticket-list.module').then(m => m.TicketListModule)
  },
  {
    path: 'find-partner',
    //canActivate: [userGuard],
    loadChildren: () => import('../find-partner/find-partner.module').then(m => m.FindPartnerModule)
  },

  {
    path: 'api-info',
    //canActivate: [userGuard],
    loadChildren: () => import('../api-info/api-info.module').then(m => m.ApiInfoModule)
  },
  {
    path: 'user-config',
    // canActivate: [userGuard],
    loadChildren: () => import('./user-config/user-config.module').then(m => m.UserConfigModule)
  },
  {
    path: '**',
    redirectTo: 'ticket-list'
  }
];
@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class AdminRoutingModule {
}