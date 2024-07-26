import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppMenuComponent } from './app-menu.component';
import { AppMenuItemComponent } from './app-menuitem.component';
import { RouterModule } from '@angular/router';
import { TooltipModule } from 'primeng/tooltip';
import { RippleModule } from 'primeng/ripple';



@NgModule({
  declarations: [
    AppMenuComponent,
    AppMenuItemComponent
  ],
  exports:[AppMenuComponent,
    AppMenuItemComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    TooltipModule,
    RippleModule
  ]
})
export class AppMenuModule { }
