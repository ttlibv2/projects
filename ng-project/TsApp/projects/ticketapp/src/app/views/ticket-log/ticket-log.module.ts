import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TicketLogRoutingModule } from './ticket-log-routing.module';
import { TicketLogComponent } from './ticket-log.component';


@NgModule({
  declarations: [
    TicketLogComponent
  ],
  imports: [
    CommonModule,
    TicketLogRoutingModule
  ]
})
export class TicketLogModule { }
