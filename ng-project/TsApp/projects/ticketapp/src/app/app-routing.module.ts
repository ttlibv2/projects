import {NgModule} from '@angular/core';
import {Route, RouterModule, Routes} from '@angular/router';
import {userGuard} from "./guards/auth.guard";
import { AppLayoutModule, AppLayoutComponent } from 'ts-layout';

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./views/auth/auth.module').then(m => m.AuthModule)
  },
 
  {
    path: '**',
    pathMatch: 'full', redirectTo: '/ticket-form'
  },
];

const appRoutes: Route[] = [
  { 
    path: '', component: AppLayoutComponent,
    children: [
      {
        path: 'find-partner',
        loadChildren: () => import('./views/find-partner/find-partner.module').then(m => m.FindPartnerModule)
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
        path: 'find-partner',
        canActivate: [userGuard],
        loadChildren: () => import('./views/find-partner/find-partner-routing.module').then(m => m.FindPartnerRoutingModule)
      },
      { 
        path: 'template',
        canActivate: [userGuard], loadChildren: () => import('./views/template-form/template-form.module').then(m => m.TemplateFormModule)
      },
      {
        path: '**',
        pathMatch: 'full', redirectTo: '/find-partner'
      }
      
    ]
  },
  {
    path: 'auth',
    loadChildren: () => import('./views/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: '**',
    pathMatch: 'full', redirectTo: '/find-partner'
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