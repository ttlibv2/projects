import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppFooterComponent } from './app-footer.component';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';


@NgModule({
  declarations: [AppFooterComponent],
  exports: [AppFooterComponent],
  imports: [ 
      CommonModule , 
    ButtonModule, 
    RippleModule
  ]
})
export class AppFooterModule { }
