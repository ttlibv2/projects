import {Dexie, Table} from "dexie";
import {LocalDbConfig, LocalDbError} from "./localdb.config";
import {Observable, Observer, of, switchMap} from "rxjs";
import {BiFunction, Objects, TsMap} from "ts-ui/helper";
import {DbSchema} from "./schema";
const  {isNull} = Objects;

export class DexieDb {
    private readonly dexie: Dexie;
    private readonly schema = new DbSchema();
    private readonly tables = new TsMap<string, DexieTb>();

    constructor(private readonly config: LocalDbConfig) {
        this.dexie = new Dexie(config.dbName, config.option);
    }

    initialize(): Observable<DexieDb> {
        const self = this;
        return this.createSchema().pipe(switchMap(schema => {
            this.dexie.version(this.config.dbVersion).stores(schema.getInit());
            return of(self);
        }));
    }

    open(): Observable<DexieDb> {
        const self = this;
        return new Observable((observer: Observer<DexieDb>) => {
            this.dexie.open()
                .catch(reason => {
                    console.error(`Đã xảy ra lỗi mở database ${this.dexie.name} version ${this.dexie.verno}: `, reason);
                    observer.error(reason);
                })
                .then(data => {
                    console.log(` Khởi tạo database thành công`);
                    observer.next(self);
                });
        });
    }

    /**
     * Get or create dexie table
     * @param name the table name
     * @throws error if `name` not exists in schema
     * */
    get<E extends DexieTb>(name: string):E {
        if(this.tables.has(name)) return <any>this.tables.get(name);
        else {
            const tb = this.dexie.tables.find(t => t.name === name);
            if(isNull(tb)) throw new LocalDbError(`The table ${name} not exists`);
            else {
               const newTableFnc = this.schema.get(name).table();
                const newTable = newTableFnc(this, tb);
                this.tables.set(name, newTable);
                return <any>newTable;
            }
        }
    }


    private createSchema(): Observable<DbSchema> {
        const {schema} = this.config;
        return new Observable((observer: Observer<DbSchema>) => {
            this.schema.clear();
            schema.forEach(sc => {
                sc.tableClass = sc.tableClass ?? DexieTb;
                sc.table = () => (db:DexieDb, tb:Table) => new sc.tableClass(db, tb);
                this.schema.set(sc.name, sc);
            });
            observer.next(this.schema);
            observer.complete();
        });
    }
}

export class DexieTb {


    constructor( protected readonly db: DexieDb,
    protected readonly tb: Table) {
    }
}