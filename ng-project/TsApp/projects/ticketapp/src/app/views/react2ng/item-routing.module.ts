import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Rect2NgComponent } from './item.component';

const routes: Routes = [{ path: '', component: Rect2NgComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Rect2NgRoutingModule { }
