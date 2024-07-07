import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GroupHelpRoutingModule } from './group-help-routing.module';
import { GroupHelpComponent } from './group-help.component';


@NgModule({
  declarations: [
    GroupHelpComponent
  ],
  imports: [
    CommonModule,
    GroupHelpRoutingModule
  ]
})
export class GroupHelpModule { }
