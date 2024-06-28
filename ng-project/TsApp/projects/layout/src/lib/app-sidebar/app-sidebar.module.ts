import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppSidebarComponent } from './app-sidebar.component';
import { AppMenuModule } from '../app-menu/app-menu.module';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    AppSidebarComponent
  ],
  exports:[AppSidebarComponent],
  imports: [
    CommonModule,
    RouterModule,
    AppMenuModule
  ]
})
export class AppSidebarModule { }
