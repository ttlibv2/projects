import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {path: '', loadChildren: () => import('./views/send-form/send-form.module').then(m => m.SendFormModule)},
  {path: '**', pathMatch: 'full',redirectTo:''}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
    bindToComponentInputs: true
  })
],
  exports: [RouterModule]
})
export class AppRoutingModule { }
