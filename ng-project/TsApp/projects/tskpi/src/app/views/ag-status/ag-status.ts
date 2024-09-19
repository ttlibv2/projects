import { ICellRendererAngularComp } from "@ag-grid-community/angular";
import { GridApi, ICellRendererParams } from "@ag-grid-community/core";
import { CommonModule, NgIf } from "@angular/common";
import { Component } from "@angular/core";
import { Objects } from "ts-ui/helper";

export type AgStatus = 'loading' | 'success' | 'error' | undefined ;

@Component({
    standalone: true,
    selector: '[ts-ag-cell-status]',
    imports: [CommonModule, NgIf],
    template: `<span [ngClass]="iconClass"></span>`
})
export class AgStatusRenderer implements ICellRendererAngularComp {
    state: AgStatus = 'success';
    gridApi: GridApi;

    get isLoading(): boolean {
        return this.state == 'loading';
    }

    get isOk(): boolean {
        return this.state == 'success';
    }

    get isError(): boolean {
        return this.state == 'error';
    }
    
    constructor(){}

    agInit(params: ICellRendererParams<any, any, any>): void {
        this.state = params.value;
    }

    refresh(params: ICellRendererParams<any, any, any>): boolean {
        this.state = params.value;
        return Objects.notBlank(this.state);
    }


    get iconClass(): any {
        return {
            ['pi pi-icon']: Objects.notBlank(this.state),
            ['pi-icon-state-'+this.state]:  Objects.notBlank(this.state),
            'pi-sync pi-spin': this.state === 'loading',
            'pi-verified': this.state === 'success',
            'pi-exclamation-triangle': this.state === 'error',
            
        };
    }

}