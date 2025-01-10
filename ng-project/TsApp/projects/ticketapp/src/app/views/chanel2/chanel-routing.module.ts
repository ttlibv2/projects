import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChanelComponent } from './chanel.component';
import { ChanelModule } from './chanel.module';

const routes: Routes = [{ path: '', component: ChanelComponent }];

@NgModule({
  imports: [ChanelModule, RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ChanelRoutingModule { }
