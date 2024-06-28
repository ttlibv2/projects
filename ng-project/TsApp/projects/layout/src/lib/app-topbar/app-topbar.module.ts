import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppTopbarComponent } from './app-topbar.component';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { RippleModule } from 'primeng/ripple';


@NgModule({
  declarations: [
    AppTopbarComponent
  ],
  exports: [AppTopbarComponent],
  imports: [
    CommonModule,
    RouterLink,
    RippleModule,
    ButtonModule,
    BadgeModule
  ]
})
export class AppTopbarModule { }
