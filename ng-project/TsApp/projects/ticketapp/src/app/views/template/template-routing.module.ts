import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TemplateFormComponent } from './template-form.component';
import { EmailTemplateView } from '../email-template/email-template.view';
import { EmailTemplateModule } from '../email-template/email-template.module';

const routes: Routes = [
  { path: '', component: TemplateFormComponent },
  { path: 'email', component: EmailTemplateView }
];

@NgModule({
  imports: [RouterModule.forChild(routes), EmailTemplateModule],
  exports: [RouterModule]
})
export class TemplateRoutingModule { }
