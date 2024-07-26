import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppRightMenuComponent } from './app-rightmenu.component';



@NgModule({
  declarations: [
    AppRightMenuComponent
  ],
  exports:[AppRightMenuComponent],
  imports: [
    CommonModule
  ]
})
export class AppRightmenuModule { }
