import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TagComponent } from './tag.component';
import { RippleModule } from 'primeng/ripple';


@NgModule({
  declarations: [ 
    TagComponent
  ],
  imports: [ 
    CommonModule,
    RippleModule
  ],
  exports: [
    TagComponent
  ]
})
export class TagModule { }
