import {Inject, Injectable} from "@angular/core";
import {DB_CONFIG_TOKEN, LocalDbConfig} from "./localdb.config";
import {DexieDb, DexieTb} from "./dexie.db";
import {Observable} from "rxjs";

@Injectable()
export class LocalDbService {
    private readonly dexie: DexieDb;

    constructor(@Inject(DB_CONFIG_TOKEN) private config: LocalDbConfig) {
        this.dexie = new DexieDb(config);
    }

    initializeDb(openDb: boolean = true): Observable<DexieDb> {
        const result$ = this.dexie.initialize();
        return openDb ? this.openDatabase() : result$;
    }

    openDatabase():Observable<DexieDb> {
        return this.dexie.open();
    }

    getTable<E extends DexieTb>(name: string): E {
        return this.dexie.get(name);
    }














}