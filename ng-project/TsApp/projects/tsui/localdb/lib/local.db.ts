import Dexie from "dexie";
import { Observable, Observer} from "rxjs";
import {DBConfig, TableId} from "./local.config";
import {Objects, TsMap} from "ts-ui/helper";
import {LocalTable} from "./local.table";
import {DbSchema, TbSchema} from "./local.schema";

Dexie.debug = true;

export class LocalDb {
    private readonly dexie: Dexie;
    private readonly dbSchema = new DbSchema();
    private readonly tables = new TsMap<string, LocalTable>();

    constructor(private dbConfig: DBConfig) {
        const {dbName, options} = dbConfig;
        this.dexie = new Dexie(dbName, options);
        this.dexie._dbSchema
    }

    initialize(): Observable<LocalDb> {
        return new Observable((observer: Observer<LocalDb>) => {
            const {dbVersion, schema} = this.dbConfig;

            //-- init table for database
            const store = Objects.arrayToJson(schema, item => [item.name, item.keyPath]);
            this.dexie.version(dbVersion).stores(store);

            // init after
            const schemas = schema.map(sc => TbSchema.from(sc));
            this.dbSchema.clear().setAll(schemas, tb => {
                const dexieTb = this.dexie.table(tb.name);
                dexieTb.schema.mappedClass = tb.entityClass;

                tb.indexes  = dexieTb.schema.indexes;
                tb.primKey =  dexieTb.schema.primKey;
                tb.dexieTb = dexieTb;
                return tb.name;
            });

            //open db
            this.dexie.open()
                .catch(reason => observer.error(reason))
                .then(_ => {
                    observer.next(this);
                    observer.complete();
                });
        })

    }

    get<E, Id extends TableId, T extends LocalTable<E, Id>>(name: string): T {
        if(!this.dbSchema.has(name)) {
            throw new Error(`The table ${name} not exist.`);
        }
        else if(this.tables.has(name)) {
            return <any>this.tables.get(name);
        }
        else {
            const schema = this.dbSchema.get(name);
            const localTb = schema.newTableFunc(this);
            this.tables.set(name, localTb);
            return localTb;
        }
    }



}