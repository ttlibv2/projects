import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {TableColumn, TableOption} from "ts-ui/ag-table";
import {FormGroup, FormsBuilder} from "ts-ui/forms";
import {ToastService} from "ts-ui/toast";
import {Callback} from "ts-ui/helper";
import {Validators} from "@angular/forms";

const columns: TableColumn[] = [
    {field: 'software_id', headerName: 'ID'},
    {field: 'code', headerName: 'Code'},
    {field: 'value', headerName: 'Value'},
    {field: 'soft_names', headerName: 'Names'}
];

@Component({
    selector: 'ts-software',
    templateUrl: './software.component.html',
    styleUrl: './software.component.scss'
})
export class SoftwareComponent {
    columns: TableColumn[] = [
        {field: 'software_id', headerName: 'ID'},
        {field: 'code', headerName: 'Code'},
        {field: 'value', headerName: 'Value'},
        {field: 'soft_names', headerName: 'Names'}
    ];

    formBuild = (fb: FormsBuilder) => fb.group({
        software_id: [null],
        code: [null, Validators.required],
        value: [null, Validators.required],
        soft_names: [null, Validators.required]
    });


}
