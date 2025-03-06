import {Inject, Injectable} from "@angular/core";
import {DB_CONFIG_TOKEN, DBConfig, TableId} from "./local.config";
import {Observable} from "rxjs";
import {LocalDb} from "./local.db";
import {LocalTable} from "./local.table";

@Injectable({providedIn: 'root'})
export class DBService {
    private readonly localDb: LocalDb;

    constructor(@Inject(DB_CONFIG_TOKEN) private dbConfig: DBConfig) {
        this.localDb = new LocalDb(dbConfig);
    }



    initDatabase(): Observable<any> {
        return this.localDb.initialize();
    }

    getTable<E, Id extends TableId, T extends LocalTable<E, Id>>(name: string): T {
        return this.localDb.get(name);
    }

}