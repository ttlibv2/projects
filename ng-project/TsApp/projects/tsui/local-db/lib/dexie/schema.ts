import {TsMap} from "ts-ui/helper";
import {TableSchema} from "./localdb.config";


export class DbSchema extends TsMap<string, TableSchema>{

    getInit(): {[table: string]:string} {
        const array = [...this.keys()].map(k => [k, this.get(k).keyPath]);
        return Object.fromEntries(array);
    }
}