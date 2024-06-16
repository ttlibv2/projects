import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FindPartnerComponent } from './find-partner.component';

const routes: Routes = [{ path: '', component: FindPartnerComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FindPartnerRoutingModule { }
