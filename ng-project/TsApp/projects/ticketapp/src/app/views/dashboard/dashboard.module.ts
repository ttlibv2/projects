import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { StringTemplateOutlet } from 'ts-ui/core';
import { InputGroupModule } from 'ts-ui/input-group';
import { Icon } from 'ts-ui/icon';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';

@NgModule({
  declarations: [
    DashboardComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    StringTemplateOutlet,
    InputGroupModule,
    InputTextModule,
    InputTextareaModule,
    InputNumberModule,
    ButtonModule,
    Icon

  ]
})
export class DashboardModule { }
