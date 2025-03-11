import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TableInfoComponent } from './table-info.component';

const routes: Routes = [{ path: '', component: TableInfoComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TableInfoRoutingModule { }
