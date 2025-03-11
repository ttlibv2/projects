import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ForgotPwdRoutingModule } from './forgot-pwd-routing.module';
import { ForgotPwdComponent } from './forgot-pwd.component';


@NgModule({
  declarations: [
    ForgotPwdComponent
  ],
  imports: [
    CommonModule,
    ForgotPwdRoutingModule
  ]
})
export class ForgotPwdModule { }
