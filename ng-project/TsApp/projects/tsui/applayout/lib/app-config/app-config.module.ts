import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppConfigComponent } from './app-config.component';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { SidebarModule } from 'primeng/sidebar';
import { MenuModule } from 'primeng/menu';
import { RadioButtonModule } from 'primeng/radiobutton';
import { CheckboxModule } from 'primeng/checkbox';
import { FormsModule } from '@angular/forms';
import { ToolbarModule } from 'primeng/toolbar';
import { InputSwitchModule } from 'primeng/inputswitch';

@NgModule({
  declarations: [
    AppConfigComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ToolbarModule,
    SidebarModule,
    RadioButtonModule,
    InputSwitchModule,
    CheckboxModule,
    ButtonModule,
    RippleModule,
    MenuModule
  ],
  exports: [AppConfigComponent]
})
export class AppConfigModule { }
