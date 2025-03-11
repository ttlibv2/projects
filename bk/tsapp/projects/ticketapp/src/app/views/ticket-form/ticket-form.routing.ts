import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TicketFormComponent } from './ticket-form.component';
import {TicketFormModule} from "./ticket-form.module";

const routes: Routes = [{ path: '', component: TicketFormComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes), TicketFormModule],
  exports: [RouterModule]
})
export class TicketFormRouting { }
