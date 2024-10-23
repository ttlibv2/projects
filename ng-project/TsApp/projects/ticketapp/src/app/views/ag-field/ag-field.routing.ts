import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AgTbComponent } from './table.component';
import { AllView } from './all.component';

const routes: Routes = [{ path: '', component: AllView }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AgFieldRouting { }
 