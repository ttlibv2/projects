import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChanelComponent } from './chanel.component';
import { FormTableModule } from '../shared/mvc2/formql';


@NgModule({
  declarations: [
    ChanelComponent
  ],
  imports: [
    CommonModule,
    FormTableModule
  ]
})
export class ChanelModule { }
