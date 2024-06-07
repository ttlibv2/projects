import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserConfigRoutingModule } from './user-config-routing.module';
import { UserConfigComponent } from './user-config.component';


@NgModule({
  declarations: [
    UserConfigComponent
  ],
  imports: [
    CommonModule,
    UserConfigRoutingModule
  ]
})
export class UserConfigModule { }
