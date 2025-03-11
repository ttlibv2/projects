import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgTbComponent } from './table.component';
import { AgFieldRouting } from './ag-field.routing';
import { FormsModule } from 'ts-ui/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { DividerModule } from 'primeng/divider';
import { CardModule } from 'primeng/card';
import { SplitterModule } from 'primeng/splitter';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { TreeModule } from 'primeng/tree';
import { FieldsetModule } from 'primeng/fieldset';
import { AgTableModule } from 'ts-ui/agtable';
import { AgFieldComponent } from './field.component';
import { MvcModule } from '../shared/mvc/mvc.module';
import { AllView } from './all.component';
import { CheckboxModule } from 'primeng/checkbox';
import { MultiSelectModule } from 'primeng/multiselect';


@NgModule({
  declarations: [
    AgTbComponent,
    AgFieldComponent,
    AllView
  ],
  imports: [
    CommonModule,
    FormsModule,
    MvcModule,
    AgFieldRouting,
    AgTableModule,
    SplitterModule,
    TreeModule,
    ButtonModule,
    InputTextModule,
    InputTextareaModule,
    InputNumberModule,
    MultiSelectModule,
    CheckboxModule,
    FieldsetModule,
    DropdownModule,
    CardModule,
    DividerModule
  ]
})
export class AgFieldModule { }
