import { NgModule } from '@angular/core';
import { CommonModule, JsonPipe } from '@angular/common';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import {StatusPageView} from "ts-ui/http-status";
import { BorderPanel } from 'ts-ui/border-panel';
import { AnyTemplateOutlet } from 'ts-ui/common';
import { Card } from 'ts-ui/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SideItemView } from 'ts-ui/layout';

@NgModule({
  declarations: [
    DashboardComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    ButtonModule,
    StatusPageView,
    InputTextModule,
    BorderPanel,
    AnyTemplateOutlet,
    Card,
    JsonPipe,
    SideItemView
  ]
})
export class DashboardModule { }