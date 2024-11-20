import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TemplateFormComponent } from './template-form.component';
import { EmailTemplateModule } from '../email-template/email-template.module';

const routes: Routes = [
  { 
    path: '', component: TemplateFormComponent 
  },
  {
    path: 'email',
    loadChildren: () => import('../email-template/email-template.routing').then(m => m.EmailTemplateRouting)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes), EmailTemplateModule],
  exports: [RouterModule]
})
export class TemplateRoutingModule { }
