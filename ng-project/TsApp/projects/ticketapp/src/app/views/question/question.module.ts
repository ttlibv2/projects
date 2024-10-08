import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { QuestionRoutingModule } from './question-routing.module';
import { QuestionComponent } from './question.component';
import { MvcModule } from '../shared/mvc/mvc.module';


@NgModule({
  declarations: [
    QuestionComponent
  ],
  imports: [
    CommonModule,
    QuestionRoutingModule,
    MvcModule
  ]
})
export class QuestionModule { }
