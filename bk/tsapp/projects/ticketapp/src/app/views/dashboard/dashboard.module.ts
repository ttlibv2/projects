import { NgModule } from '@angular/core';
import { CommonModule, JsonPipe } from '@angular/common';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import {StatusPageView} from "ts-ui/httpstatus";
import { BorderPanel } from 'ts-ui/borderpanel';
import { AnyTemplateOutlet } from 'ts-ui/common';
import { Card } from 'ts-ui/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SideItemView } from 'ts-ui/layout';
import { SideMenu } from './side/side-menu';

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
    SideMenu
  ]
})
export class DashboardModule { }