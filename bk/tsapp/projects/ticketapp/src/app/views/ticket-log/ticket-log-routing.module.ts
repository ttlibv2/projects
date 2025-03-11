import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TicketLogComponent } from './ticket-log.component';

const routes: Routes = [{ path: '', component: TicketLogComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TicketLogRoutingModule { }
