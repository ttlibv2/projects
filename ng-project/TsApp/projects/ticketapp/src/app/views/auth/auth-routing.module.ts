import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { userGuard } from '../../guards/auth.guard';

const routes: Routes = [
  {
    path: 'signin',
    loadChildren: () => import('./signin/signin.module').then(m => m.SigninModule)
  },
  {
    path: 'signup',
    loadChildren: () => import('./signup/signup.module').then(m => m.SignupModule)
  },

  {
    path: 'forgot-pwd',
    loadChildren: () => import('./forgot-pwd/forgot-pwd.module').then(m => m.ForgotPwdModule)
  },
  {
    path: 'change-pwd',
    canActivate: [userGuard], loadChildren: () => import('./change-pwd/change-pwd.module').then(m => m.ChangePwdModule)
  },
  {
    path: '**',
    pathMatch: 'prefix', redirectTo: 'signin'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {
}
