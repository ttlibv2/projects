import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TicketListRoutingModule } from './ticket-list-routing.module';
import { TicketListComponent } from './ticket-list.component';


@NgModule({
  declarations: [
    TicketListComponent
  ],
  imports: [
    CommonModule,
    TicketListRoutingModule
  ]
})
export class TicketListModule { }
