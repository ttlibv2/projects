import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ChangePwdRoutingModule } from './change-pwd-routing.module';
import { ChangePwdComponent } from './change-pwd.component';


@NgModule({
  declarations: [
    ChangePwdComponent
  ],
  imports: [
    CommonModule,
    ChangePwdRoutingModule
  ]
})
export class ChangePwdModule { }
