import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {path: 'signin', loadChildren: () => import('./signin/signin.module').then(m => m.SigninModule)},
  {path: 'signup', loadChildren: () => import('./signup/signup.module').then(m => m.SignupModule)},
  {path: 'error', loadChildren: () => import('./error/error.module').then(m => m.ErrorModule)},
  {path: 'forgot-pwd', loadChildren: () => import('./forgot-pwd/forgot-pwd.module').then(m => m.ForgotPwdModule)},
  {path: 'change-pwd', loadChildren: () => import('./change-pwd/change-pwd.module').then(m => m.ChangePwdModule)},
  {path: '**', pathMatch: 'full', redirectTo: 'signin'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {
}
