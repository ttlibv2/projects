import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppBreadcrumbComponent } from './app-breadcrumb.component';
import {BreadcrumbModule } from 'primeng/breadcrumb';



@NgModule({
  declarations: [
    AppBreadcrumbComponent
  ],
  exports: [AppBreadcrumbComponent],
  imports: [
    CommonModule,
    BreadcrumbModule
  ]
})
export class AppBreadcrumbModule { }
