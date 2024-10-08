import { Component, OnInit } from '@angular/core';
import { TableColumn } from "ts-ui/ag-table";
import { FormsBuilder } from "ts-ui/forms";
import { Validators } from "@angular/forms";
import { Field, MvcOption } from "../shared/mvc/mvc.component";
import { SoftwareService } from '../../services/software.service';
import { Objects } from 'ts-ui/helper';

@Component({
    selector: 'ts-software',
    templateUrl: './software.component.html',
    styleUrl: './software.component.scss'
})
export class SoftwareComponent {

    constructor(private fb: FormsBuilder,
        private softSrv: SoftwareService) { }


    formGroup = this.fb.group({
        id: [null],
        code: [null, Validators.required],
        value: [null, Validators.required],
        soft_names: [null, Validators.required]
    });

    mvcOption: MvcOption<any> = {
        visibleExport: true,
        visibleImport: true,
        autoLoadData: true,
        rowNameId: 'id',
        
        formFields: [
            { fieldId: 'code', label: 'Mã phần mềm', type: 'input', class: 'lg:col-4' },
            { fieldId: 'value', label: 'Tên phần mềm', type: 'input', class: 'lg:col-4' },
            { fieldId: 'soft_names', label: 'DS Tên', type: 'input', class: 'lg:col-4' },
        ],

        resetDataFunc: () => { },
        loadDataFunc: () => this.softSrv.findAll(),
        editDataFunc: data => this.softSrv.updateById(data.id, data),
        deleteDataFunc: data => this.softSrv.deleteById(data.id),
        newDataFunc: data => {
            const names: string = <any>data.soft_names;
            if (Objects.notBlank(names)) data.soft_names = names.split(',');
            return this.softSrv.createNew(data)
        },

    }




}
