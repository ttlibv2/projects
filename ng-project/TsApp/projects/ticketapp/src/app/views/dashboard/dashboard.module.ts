import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { StringTemplateOutlet } from 'ts-ui/core';
//import { InputGroupModule } from 'ts-ui/input-group';
import { Icon } from 'ts-ui/icon';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { ToolBar } from 'ts-ui/toolbar';
import { ColorBlock, ColorPickerPanel, InputColor } from 'ts-ui/color-picker';
import { FormsModule as JFormsModule } from 'ts-ui/forms';
import { FormsModule } from '@angular/forms';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { ChipsModule } from 'primeng/chips';

@NgModule({
  declarations: [
    DashboardComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    JFormsModule,
    FormsModule,
    //StringTemplate,
    //InputGroupModule,
    InputTextModule,
    InputTextareaModule,
    InputNumberModule,
    ButtonModule,
	OverlayPanelModule, InputGroupModule, InputGroupAddonModule, ButtonModule, InputTextModule, ChipsModule, 
    Icon,
    ToolBar,
    ColorPickerPanel,
    InputColor,
    ColorBlock

  ]
})
export class DashboardModule { }