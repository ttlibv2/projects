import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SoftwareRoutingModule } from './software-routing.module';
import { SoftwareComponent } from './software.component';


@NgModule({
  declarations: [
    SoftwareComponent
  ],
  imports: [
    CommonModule,
    SoftwareRoutingModule
  ]
})
export class SoftwareModule { }
