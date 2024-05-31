import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {path: 'auth', loadChildren: () => import('./views/auth/auth.module').then(m => m.AuthModule)},
  { path: 'ticket-form', loadChildren: () => import('./views/ticket-form/ticket-form.module').then(m => m.TicketFormModule) },
  { path: 'find-partner', loadChildren: () => import('./views/find-partner/find-partner.module').then(m => m.FindPartnerModule) },
  {path: '**', pathMatch: 'full', redirectTo: '/find-partner'},
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
