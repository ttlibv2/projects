import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SendFormView } from './send-form.view';
import { RouterModule, Routes } from '@angular/router';
import { AgTableModule } from 'ts-ui/ag-table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { SelectFileModule } from '../select-file/select-file.module';
import { DividerModule } from 'primeng/divider';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';


const router:Routes = [
  {path: '', component:SendFormView}
];


@NgModule({
  declarations: [SendFormView],
  imports: [
    CommonModule,
    ButtonModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    InputGroupModule,
    InputGroupAddonModule,
    CardModule,
    SelectFileModule,
    DividerModule,
    RouterModule.forChild(router),
    AgTableModule.forRoot()
  ]
})
export class SendFormModule { }
