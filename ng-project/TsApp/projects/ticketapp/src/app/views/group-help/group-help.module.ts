import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GroupHelpRoutingModule } from './group-help-routing.module';
import { GroupHelpComponent } from './group-help.component';
import { MvcModule } from '../shared/mvc/mvc.module';


@NgModule({
  declarations: [
    GroupHelpComponent
  ],
  imports: [
    CommonModule,
    GroupHelpRoutingModule,
    MvcModule
  ]
})
export class GroupHelpModule { }
