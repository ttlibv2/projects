import { ICellEditorAngularComp } from "@ag-grid-community/angular";
import { ICellEditorParams } from "@ag-grid-community/core";
import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { InputTextModule } from "primeng/inputtext";

@Component({
    selector: 'ts-primeng-cell-editor',
    standalone: true,
    imports:[CommonModule, InputTextModule],
    template:`
    <input pInputText class="ag-input-field-input ag-text-field-input"/>

    `
})
export class PrimeNgCellEditor implements ICellEditorAngularComp {

    agInit(params: ICellEditorParams<any, any, any>): void {
        console.log(`pCellEditor => agInit: `, params);
    }

    getValue()  {
        
    }
}