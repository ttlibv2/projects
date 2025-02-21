import {Objects} from "./objects";

export class ListError extends Error {
}

const {notNull} = Objects;

function validateIndex(index: number, array: any[]) {
    if (index < 0 || index >= array.length) {
        throw new ListError(`The index [${index}] missing`);
    }
}

export class List<E> {
    private readonly array: E[] = [];

    get(index: number): E {
        validateIndex(index, this.array);
        return this.array[index];
    }

    add(item: E): this {
        this.array.push(item);
        return this;
    }

    deleteAt(index: number): this {
        this.array.splice(index, 1);
        return this;
    }

    delete(item: E): this {
        const index = this.array.findIndex(i => Objects.equals(item, i));
        if (index && index >= 0) this.deleteAt(index);
        return this;
    }

    set(index: number, item: E): this {
        validateIndex(index, this.array);
        this.array.splice(index, 1, item);
        return this;
    }

    setAll(items: List<E> | E[]): this {
        if (items instanceof List) this.array.push(...items.array);
        else if (notNull(items)) this.array.push(...items);
        return this;
    }

}