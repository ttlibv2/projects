import { ChangeDetectorRef, Component } from "@angular/core";
import { ICellRendererAngularComp } from "ag-grid-angular";
import { ICellRendererParams } from "ag-grid-community";
import { Objects } from "ts-helper";
const {notNull} = Objects;

@Component({
    selector: '[p-ticket-cell]',
    templateUrl: './ag-cell.html' 
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