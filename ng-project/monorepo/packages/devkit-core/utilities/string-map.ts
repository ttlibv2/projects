export type StringMapListener<V extends object> = (
    name: string,
    newValue: V | undefined,
    collection: StringMap<V>
) => void;

export class StringMap<V extends object> implements ReadonlyMap<string, V> {
    readonly _map: Map<string, V>;

    constructor(
        initial?: Record<string, V>,
        private _listener?: StringMapListener<V>) {
        this._map = new Map(initial && Object.entries(initial));
    }

    delete(key: string): boolean {
        const result = this._map.delete(key);

        if (result) {
            this._listener?.(key, undefined, this);
        }

        return result;
    }

    set(key: string, value: V): this {
        const updatedValue = value !== this.get(key);

        if (updatedValue) {
            this._map.set(key, value);
            this._listener?.(key, value, this);
        }

        return this;
    }

    forEach<T>(callbackFn: (value: V, key: string, map: StringMap<V>) => void, thisArg?: T): void {
        this._map.forEach((value, key) => callbackFn(value, key, this), thisArg);
    }

    get(key: string): V | undefined {
        return this._map.get(key);
    }

    has(key: string): boolean {
        return this._map.has(key);
    }

    get size(): number {
        return this._map.size;
    }

    [Symbol.iterator](): MapIterator<[string, V]> {
        return this._map[Symbol.iterator]();
    }

    entries(): MapIterator<[string, V]> {
        return this._map.entries();
    }

    keys(): MapIterator<string> {
        return this._map.keys();
    }

    values(): MapIterator<V> {
        return this._map.values();
    }

}