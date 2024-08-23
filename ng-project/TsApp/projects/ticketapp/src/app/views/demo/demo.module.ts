import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DemoRoutingModule } from './demo-routing.module';
import { DemoComponent } from './demo.component';
import { LabelComponent } from './label.component';
import {TooltipModule} from "primeng/tooltip";
import { FieldComponent } from './field/field.component';


@NgModule({
  declarations: [
    DemoComponent,
    LabelComponent,
    FieldComponent
  ],
  imports: [
    CommonModule,
    DemoRoutingModule,
      TooltipModule
  ],
  exports: [
  ]
})
export class DemoModule { }