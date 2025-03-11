import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ChanelRoutingModule } from './chanel-routing.module';
import { ChanelComponent } from './chanel.component';
import { MvcModule } from '../shared/mvc/mvc.module';


@NgModule({
  declarations: [
    ChanelComponent
  ],
  imports: [
    CommonModule,
    ChanelRoutingModule,
    MvcModule
  ]
})
export class ChanelModule { }
