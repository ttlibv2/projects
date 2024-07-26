import { Component, Input } from "@angular/core";
import { ICellRendererAngularComp } from "@ag-grid-community/angular";
import { ICellRendererParams } from "@ag-grid-community/core";

@Component({
    selector: '[ts-ag-cell-color]',
    template: `<p-colorPicker [ngModel]="color"></p-colorPicker>`
})
export class AgCellColor implements ICellRendererAngularComp {
    @Input() color: string;

    agInit(params: ICellRendererParams<any, any, any>): void {
        this.setColor(params);
    }

    refresh(params: ICellRendererParams<any, any, any>): boolean {
        this.setColor(params);
        return true;
    }

    private setColor(params: ICellRendererParams<any, any, any>) {
        const field = params.colDef.field;
        this.color = params.value;//params.value ? params.value[field] : undefined;
        console.log(this.color)
    }
}