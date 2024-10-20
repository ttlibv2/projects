import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';

import { TicketListRoutingModule } from './ticket-list-routing.module';
import { TicketListComponent } from './ticket-list.component';
import { AgTableModule } from 'ts-ui/ag-table';
import { TsLoggerModule } from 'ts-ui/logger';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { CardModule } from 'primeng/card';
import { MultiSelectModule } from 'primeng/multiselect';
import { DropdownModule } from 'primeng/dropdown';
import { CheckboxModule } from 'primeng/checkbox';
import { DividerModule } from 'primeng/divider';
import { SplitButtonModule } from 'ts-ui/split-button';
import { AgStatusRenderer, AgTagCell, AgTicketCell } from './ag-ticket-cell';
import { IconFieldModule } from 'primeng/iconfield';
import { ChipModule } from 'primeng/chip';
import { TagModule } from 'ts-ui/tag';
import { AgTableTemplate } from './ag-table-template';
import { TicketFormModule } from '../ticket-form/ticket-form.module';
import { FileUploadModule } from 'primeng/fileupload';
import { FormsModule } from 'ts-ui/forms';
import { ResizedDirective } from '../shared/d-resized';

@NgModule({
  declarations: [
    TicketListComponent,
    AgTicketCell,
    // AgStatusRenderer,
    AgTagCell,
  ],
  imports: [
    CommonModule,
    FormsModule,
    TicketListRoutingModule,
    TsLoggerModule,
    AgTableModule,
    CardModule,
    ButtonModule,
    CalendarModule,
    MultiSelectModule,
    DropdownModule,
    CheckboxModule,
    DividerModule,
    SplitButtonModule,
    IconFieldModule,
    ChipModule,
    TagModule,
    AgTableTemplate,
    TicketFormModule,
    FileUploadModule,
    ResizedDirective

  ],
  providers: [
    DatePipe,

  ]
})
export class TicketListModule { }
