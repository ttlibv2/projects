import {inject, NgModule} from '@angular/core';
import {ActivatedRouteSnapshot, RouterModule, RouterStateSnapshot, Routes} from '@angular/router';
import {userGuard} from "./guards/auth.guard";

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./views/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'ticket-form',
    canActivate: [userGuard],
    loadChildren: () => import('./views/ticket-form/ticket-form.module').then(m => m.TicketFormModule)
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
    canActivate: [userGuard], loadChildren: () => import('./views/api-info/api-info.module').then(m => m.ApiInfoModule)
  },
  {
    path: 'chanel',
    canActivate: [userGuard], loadChildren: () => import('./views/chanel/chanel.module').then(m => m.ChanelModule)
  },
  {
    path: 'question',
    canActivate: [userGuard], loadChildren: () => import('./views/question/question.module').then(m => m.QuestionModule)
  },
  {
    path: 'software',
    canActivate: [userGuard], loadChildren: () => import('./views/software/software.module').then(m => m.SoftwareModule)
  },
  {
    path: 'group-help',
    canActivate: [userGuard],
    loadChildren: () => import('./views/group-help/group-help.module').then(m => m.GroupHelpModule)
  },
  {
    path: 'user-info',
    canActivate: [userGuard],
    loadChildren: () => import('./views/user-info/user-info.module').then(m => m.UserInfoModule)
  },
  {
    path: 'table-info',
    canActivate: [userGuard],
    loadChildren: () => import('./views/table-info/table-info.module').then(m => m.TableInfoModule)
  },
  {
    path: 'ticket-log',
    canActivate: [userGuard],
    loadChildren: () => import('./views/ticket-log/ticket-log.module').then(m => m.TicketLogModule)
  },
  {
    path: 'user-config',
    canActivate: [userGuard],
    loadChildren: () => import('./views/user-config/user-config.module').then(m => m.UserConfigModule)
  },
  {
    path: '**',
    pathMatch: 'full', redirectTo: '/user-info'
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      bindToComponentInputs: true,
      enableTracing: false
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
