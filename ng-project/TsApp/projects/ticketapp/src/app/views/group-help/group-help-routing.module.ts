import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GroupHelpComponent } from './group-help.component';

const routes: Routes = [{ path: '', component: GroupHelpComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GroupHelpRoutingModule { }
