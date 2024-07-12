import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { SplitButton } from './splitbutton.component';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { AutoFocusModule } from 'primeng/autofocus';
import { ChevronDownIcon } from 'primeng/icons/chevrondown';
import { RippleModule } from 'primeng/ripple';



@NgModule({
  imports: [
    CommonModule, 
    ButtonModule,
    RippleModule,
    TieredMenuModule,
    AutoFocusModule, 
    ChevronDownIcon
  ], 
  exports: [
    SplitButton, 
    ButtonModule, 
    TieredMenuModule 

  ],
  declarations: [SplitButton]
})
export class SplitButtonModule { }
