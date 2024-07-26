import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { TranslateModule } from "@ngx-translate/core";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { CheckboxModule } from "primeng/checkbox";
import { DividerModule } from "primeng/divider";
import { DropdownModule } from "primeng/dropdown";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { RippleModule } from "primeng/ripple";
import { AgTableModule, TableColumn, TableOption } from "ts-ui/ag-table";
import { AgTable } from "../../models/ag-table";
import { DynamicDialogComponent, DynamicDialogRef } from "primeng/dynamicdialog";
import { ToastService } from "ts-ui/toast";
import {ModalService} from "../../services/ui/model.service";

@Component({
    selector: '[ts-ag-table-template]',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        AgTableModule,
        CardModule, DropdownModule,TranslateModule, 
        ButtonModule, RippleModule, CheckboxModule,
         DividerModule, InputTextModule, InputTextareaModule],
    template: `
<p-card class="grid template-form">
  <div class="col-12">
    <form [formGroup]="formGroup">
      <div class="grid">
        <div class=" p-fluid col-6 xxl:col-6">
          <label for="title">Tiêu đề</label>
          <input id="title" placeholder="{{'title.holder' | translate}}" pInputText formControlName="title" />
        </div>
        <div class=" p-fluid col-6 xxl:col-3">
          <label for="icon">Icon</label>
          <input pInputText  formControlName="icon" placeholder="pi pi-user" />
        </div>
        <div class=" p-fluid col-6 xxl:col-3">
          <label for="entity_code">Mã</label>
          <input pInputText  formControlName="code" placeholder="pi pi-user" />
        </div>
        <div class="col-12 p-fluid">
          <label for="summary">Diễn giải</label>
          <input id="summary" placeholder="{{'summary.holder' | translate}}" pInputText formControlName="summary" />
        </div>
        <div class="col-12 p-fluid">
          <label for="data">Dữ liệu</label>
          <textarea id="data" pInputTextarea formControlName="config"
            rows="2" [autoResize]="true" readonly></textarea>
        </div>
        <div class="col-12 flex gap-2 mt-3">
            <p-button type="submit" severity="help" label="Lưu lại"></p-button>
            <p-button label="Lấy danh sách"></p-button>
            <p-button label="Xóa mẫu"></p-button>

          <!-- <p-button type="submit" severity="help" label="{{'actions.'+labelSave | translate}}" [loading]="state.asyncSave"[disabled]="formGroup.invalid"></p-button>
          <p-button label="{{'actions.create' | translate}}" (onClick)="createTemplate()" ></p-button>
          <p-button label="{{'actions.copy' | translate}}" (onClick)="copyTemplate()" *ngIf="state.visibleCopy"></p-button>
          <p-button label="{{'actions.delete' | translate}}" (onClick)="deleteTemplate()" *ngIf="state.visibleDelete" ></p-button>
          <p-button label="{{'template.setting' | translate}}" (onClick)="settingData()"  *ngIf="entity.value"></p-button>
          <p-button label="Tải DS" (onClick)="loadTemplate()" [loading]="state.asyncTemplate"></p-button>
          <p-button label="Quay Lại" (onClick)="previousPage()" *ngIf="lastUrl"></p-button> -->


        </div>
      </div>
    </form>
  </div>
  <div class="col-12"  [style]="{'border-top': '2px solid red'}">
    <ts-ag-table [columns]="columns" 
        [rows]="rows" [option]="agOption"
        (rowClicked)="selectRow($event.data)"></ts-ag-table>
  </div>
</p-card>

    `
})
export class AgTableTemplate implements OnInit {
    formGroup: FormGroup;
    columns: TableColumn[] = [
        {field: 'code', headerName: 'Mã', checkboxSelection: true, headerCheckboxSelection: true},
        {field: 'title', headerName: 'Tiêu đề'},
        {field: 'svg_icon', headerName: 'Icon'},
        {field: 'summary', headerName: 'Diễn giải'},
        {field: 'position', headerName: 'Thứ tự hiển thị'}
    ];

    rows: any[] = [];

    agOption: TableOption = {
        rowSelection: 'multiple'
    };

    instance: DynamicDialogComponent;

    constructor(private fb: FormBuilder,
        private modal: ModalService,
        private dynamicRef: DynamicDialogRef) {
        this.formGroup = fb.group({
            title: [null, Validators.required],
            code: [null, Validators.required],
            position: [null, Validators.required],
            config: [null, Validators.required],
            icon: [null],  summary: [null], parent: [null],
        });
    }

    ngOnInit(): void {
        this.instance = this.modal.getInstance(this.dynamicRef);
        if(this.instance && this.instance.data) {
            const {model} = this.instance.data;
            this.rows = (<AgTable> model).children;
        }

    }

    selectRow(row: AgTable): void {
        const config = JSON.stringify(row.columns ?? row.states, null, 0);
        this.formGroup.patchValue({...row, config})
    }

}