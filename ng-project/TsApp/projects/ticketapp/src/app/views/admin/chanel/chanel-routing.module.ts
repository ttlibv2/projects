import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChanelComponent } from './chanel.component';

const routes: Routes = [{ path: '', component: ChanelComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ChanelRoutingModule { }
