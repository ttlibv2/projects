import {  Objects } from "ts-ui/helper";
import { AgColumn, AgTable } from "../../models/ag-table";

export class ListUtil {


    static updateColumn<E>(table: AgTable, override: Record<string, Partial<AgColumn>>) {
        Object.keys(override).forEach(field => {
            const column = table.get(field);
            if(Objects.notNull(column)){
                column.update(override[field]);
            }
        })
    }
}