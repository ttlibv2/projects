import {LocalDb} from "./local.db";
import {TbSchema} from "./local.schema";
import {DexieError, Table} from "dexie";
import {forkJoin, from, Observable, Observer, switchMap, tap} from "rxjs";
import {AssignObject, Callback, Objects, Page, Pageable, TsMap} from "ts-ui/helper";
import {TableId} from "./local.config";

const {isObject, isFunction, isClass, arrayToJson} = Objects;

function convertToJson(object: any): any {
    if (!isObject(object) || !isClass(object)) return object;
    else {
        const keys = Object.keys(object).filter(key => !isFunction(object[key]));
        return arrayToJson(keys, key => [key, object[key]]);
    }
}

function fromPromise<E>(promise: Promise<E>): Observable<E> {
    return from(promise);
}

export class LocalTable<E = any, Id extends TableId = any> {

    protected get tb(): Table {
        return this.schema.dexieTb;
    }

    protected get libToModel(): Callback<any, E> {
        return json => this.schema.jsonToModel(json);
    }

    protected arrayToModels(array: any[]): E[] {
        return array.map(item => this.libToModel(item));
    }

    constructor(protected readonly db: LocalDb,
                protected readonly schema: TbSchema) {
    }

    getAllToJson(): Observable<{ [key: string | number]: any }> {
        const json: Record<string, Observable<any>> = {};
        return from( this.tb.toCollection()
            .eachPrimaryKey(key => json[key] = this.findByKey(key))
           .then(_ => forkJoin(json).toPromise()));

    }

    count(): Observable<number> {
        return fromPromise(this.tb.count());
    }

    findByKey(key: Id): Observable<E> {
        return fromPromise(this.tb.get(key).then(lib => this.libToModel(lib)));
    }

    findByKeys(keys: Id[]): Observable<E[]> {
        return fromPromise(this.tb.bulkGet(keys).then(array => this.arrayToModels(array)));
    }

    getAll(): Observable<E[]> {
        return fromPromise(this.tb.toArray(ls => this.arrayToModels(ls)));
    }

    findAll(pageable: Pageable): Observable<Page<E>> {
        const page = Page.from<E>(pageable);
        const data = this.tb.limit(pageable.size).offset(pageable.offset).sortBy(this.schema.keyPath);
        return fromPromise(Promise.all([data, this.tb.count()]).then(array => {
            page.set_data(this.arrayToModels(array[0]));
            page.set_total(array[1]);
            return page;
        }));
    }

    add(item: AssignObject<E>, key?: Id): Observable<E> {
        return fromPromise(this.tb.add(item, key).then(id => this.libToModel(item)));
    }

    bulkAdd(items: AssignObject<E>[], keys?: any[]): Observable<E[]> {
        return fromPromise(this.tb.bulkAdd(items, keys).then(_ => this.arrayToModels(items)));
    }

    update(key: Id, item: AssignObject<E>): Observable<E> {
        return fromPromise(this.tb.update(key, item)
            .then(_ => this.tb.get(key))
            .then(item => this.libToModel(item)));
    }

    bulkUpdate(items: { key: Id, item: AssignObject<E> }[]): Observable<E[]> {
        return fromPromise(this.tb.bulkUpdate(<any>items)
            .then(_ => items.map(item => this.tb.get(item.key)))
            .then(array => this.arrayToModels(array)));
    }

    delete(key: Id): Observable<Id> {
        return fromPromise(this.tb.delete(key).then(_ => key));
    }

    bulkDelete(keys: Id[]): Observable<Id[]> {
        return fromPromise(this.tb.bulkDelete(keys).then(_ => keys));
    }

    put(item: AssignObject<E>, key?: Id): Observable<E> {

        return fromPromise(this.tb.put(convertToJson(item), key).then(dt => this.libToModel(dt)));
    }

    bulkPut(items: AssignObject<E>[], keys?: Id[]) {
        return fromPromise(this.tb.bulkPut(items, keys, {allKeys: true})
            .then(keys => this.tb.bulkGet(keys))
            .then(array => this.arrayToModels(array)));
    }

    setAll(items: AssignObject<E>[], keys?: Id[]): Observable<E[]> {
        return fromPromise(this.tb.clear().then(_ => this.tb.bulkPut(items, keys)));
    }

    deleteAll(): Observable<number> {
        const count = this.tb.count();
        return fromPromise(this.tb.clear().then(_ => count));
    }

    existById(key: Id): Observable<boolean> {
        return fromPromise(this.tb.where(this.tb.schema.primKey.name)
            .equals(key).count().then(c => c > 0));
    }

}