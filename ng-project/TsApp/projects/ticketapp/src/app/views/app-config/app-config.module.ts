import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppConfigComponent } from './app-config.component';
import {SidebarModule} from "primeng/sidebar";
import {RadioButtonModule} from "primeng/radiobutton";
import {InputSwitchModule} from "primeng/inputswitch";
import {FormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";

@NgModule({
  declarations: [
    AppConfigComponent
  ],
  exports: [
    AppConfigComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    SidebarModule,
    RadioButtonModule,
    ButtonModule,
    InputSwitchModule
  ]
})
export class AppConfigModule { }
