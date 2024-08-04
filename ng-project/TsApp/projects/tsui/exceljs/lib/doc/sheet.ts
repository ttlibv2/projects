import {Workbook} from "./workbook";
import {Color, SheetType} from "./common";
import {Row} from "./row";

export interface SheetOption {
    wb: Workbook;
    name: string;
    sheetId: number;
    index: number;
    sheetType: SheetType;
    hidden: boolean;
    tabColor: Color;
}

export class Sheet {
    readonly rows = new Array<Row>();

    constructor(private readonly wb: Workbook,
                private option: SheetOption) {
    }

    get name(): string {
        return this.option.name;
    }

    get sheetId(): number {
        return this.option.sheetId;
    }

    get sheetType(): SheetType {
        return this.option.sheetType;
    }

    get hidden(): boolean {
        return this.option.hidden;
    }

    get index(): number {
        return this.option.index;
    }

    get tabColor(): Color {
        return this.option.tabColor;
    }

    


}