import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ChanelRoutingModule } from './chanel-routing.module';
import { ChanelComponent } from './chanel.component';


@NgModule({
  declarations: [
    ChanelComponent
  ],
  imports: [
    CommonModule,
    ChanelRoutingModule
  ]
})
export class ChanelModule { }
