import { ChangeDetectorRef, Component } from "@angular/core";
import { ICellRendererAngularComp } from "@ag-grid-community/angular";
import { Objects } from "ts-ui/helper";
import { ICellRendererParams } from "@ag-grid-community/core";
const {notNull, isArray, isObject} = Objects;

export type AgStatus = 'loading' | 'success' | 'error' | undefined ;

@Component({ 
    selector: '[ts-ag-ticket-cell]',
    template: `
    <span class="pi pi-check" [style.color]="'var(--primary-color)'" [style.font-size]="'1.5rem'" *ngIf="isCheck"></span>
    <label [innerHTML]="data" *ngIf="isText"></label>
    <ts-tag [label]="data" *ngIf="isChip"></ts-tag>
`
})
export class AgTicketCell implements ICellRendererAngularComp {
    columnType!: 'check' | 'text' | 'chip';
    data: any;
    severity: any;

    get isCheck(): boolean { return this.columnType === "check";}
    get isChip(): boolean { return this.columnType === "chip";}
    get isText(): boolean {return this.columnType === "text";}

    constructor(private def: ChangeDetectorRef){}
  
    agInit(params: ICellRendererParams<any, any, any>): void {
       this.parseData(params);
    }

    refresh(params: ICellRendererParams<any, any, any>): boolean {
       // this.parseData(params);
        return true;
    }

    parseData(params: ICellRendererParams<any, any, any>) : void {
        const colId = params.column.getColId();
        console.log(colId, params.value)
        switch(colId) {
            case 'note_id': 
               this.data = notNull(params.value);
                this.columnType = 'check';
                break;

            case 'chanels':
                this.data = `${params.value?.length} Kênh`
                this.columnType = 'chip';
                break;

            case 'od_image':
                this.data = Object.keys(params.value);
                this.columnType = 'text';
                break;

            case 'od_partner':
                this.data = Object.keys(params.value?.display_name);
                this.columnType = 'text';
                break;

            case 'stage_text':
                this.data = params.value;
                this.severity = 'help';
                this.columnType = 'chip';
                break;

            
        }
    }
}

@Component({
    selector: '[ts-ag-tag-cell]',
    template: `<ts-tag [label]="label" [severity]="severity"></ts-tag>`
}) 
export class AgTagCell implements ICellRendererAngularComp {
    label: string;
    severity: any = "secondary";

    agInit(params: ICellRendererParams<any, any, any>): void {
        this.label = params.getValue();    
    }

    refresh(params: ICellRendererParams<any, any, any>): boolean {
        this.label = params.value;    
        return true;
    }

}




@Component({
    selector: '[ts-ag-cell-status]',
    template: `
    <span [ngClass]="iconClass">
        <label [innerHTML]="label" *ngIf="label"></label>
    </span>`
})
export class AgStatusRenderer implements ICellRendererAngularComp {
    state: AgStatus = undefined;
    label: string = undefined;

    agInit(params: ICellRendererParams<any, any, any>): void {
       this.extractData(params);
    }

    refresh(params: ICellRendererParams<any, any, any>): boolean {
        this.extractData(params);
        return true;
    }

    private extractData(params: ICellRendererParams<any, any, any>): void {
        this.state = this.hasState(params.value) ? params.value['ag_state'] : undefined;
       this.label = !this.hasState(params.value) ? params.value: undefined;
    }

    private hasState(value: any): boolean {
        return isObject(value) && 'ag_state' in value;
    }

    get iconClass(): any {
        return {
            'pi-icon pi': true,
            'pi-spin pi-cog': this.state === 'loading',
            'pi-verified': this.state === 'success',
            'pi-exclamation-triangle': this.state === 'error',
            ['pi-icon-state-'+this.state]: this.state !== undefined,
        };
    }

}