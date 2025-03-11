import * as js from 'exceljs';
import {Sheet} from "./sheet";

export class Column {

    private static from(sheet: Sheet, col: js.Column) {
        return new Column(sheet, col);
    }

    private constructor(public readonly sheet: Sheet,
                        private col: js.Column) {
    }

    get letter():string { return this.col.letter;}
    get number():number { return this.col.number;}
    get isCustomWidth():boolean { return this.col.isCustomWidth;}
    get headers():string[] { return this.col.headers;}
    get isDefault():boolean { return this.col.isDefault;}
    get headerCount():number { return this.col.headerCount;}

    /**
     * Can be a string to set one row high header or an array to set multi-row high header
     */
    get header():string | string[] { return this.col.header;}
	set header(val:string | string[]){ this.col.header=val; }

    /**
     * The name of the properties associated with this column in each row
     */
    get key():string { return this.col.key;}
	set key(val:string){ this.col.key=val; }

    /**
     * The width of the column
     */
    get width():number { return this.col.width;}
	set width(val:number){ this.col.width=val; }

    /**
     * Set an outline level for columns
     */
    get outlineLevel():number { return this.col.outlineLevel;}
	set outlineLevel(val:number){ this.col.outlineLevel=val; }

    /**
     * Hides the column
     */
    get hidden():boolean { return this.col.hidden;}
	set hidden(val:boolean){ this.col.hidden=val; }

    /**
     * Styles applied to the column
     */
    get style():Partial<js.Style> { return this.col.style;}
	set style(val:Partial<js.Style>){ this.col.style=val; }

    /**
     * The cell values in the column
     */
    get values():ReadonlyArray<js.CellValue> { return this.col.values;}
	set values(val:ReadonlyArray<js.CellValue>){ this.col.values=val; }

    /**
     * Below properties read from style
     */
    get border():Partial<js.Borders> { return this.col.border;}
	set border(val:Partial<js.Borders>){ this.col.border=val; }

    get fill():js.Fill { return this.col.fill;}
	set fill(val:js.Fill){ this.col.fill=val; }

    get numFmt():string { return this.col.numFmt;}
	set numFmt(val:string){ this.col.numFmt=val; }

    get font():Partial<js.Font> { return this.col.font;}
	set font(val:Partial<js.Font>){ this.col.font=val; }

    get alignment():Partial<js.Alignment> { return this.col.alignment;}
	set alignment(val:Partial<js.Alignment>){ this.col.alignment=val; }

    get protection():Partial<js.Protection> { return this.col.protection;}
	set protection(val:Partial<js.Protection>){ this.col.protection=val; }


}