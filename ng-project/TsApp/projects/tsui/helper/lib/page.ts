import { AssignObject } from "./common";
import { Callback } from "./function";
import {Objects} from "./objects";

export class Page<E=any> {
    data: E[] = [];
    offset: number = 0;
    limit: number = 1000;
    total: number;
    current_page: number = 1;
    sortBy: string;
    total_page?: number;
    is_first?: boolean;
    is_last?: boolean;

    set_data(data: any[]) {
        this.data = data;
        return this;
    }

    set_total(total: number) {
        this.total = total;
        return this;
    }

    update(json: AssignObject<Page<any>>): this {
        Objects.assign(this, json);
        return this;
    }

    static from<E>(json: AssignObject<Page<E>>, callback?: Callback<any, E>): Page<E> {
        const data = (json['data'] ?? []).map((item: any) => callback ? callback(item) : item);
        return json instanceof Page ? json : Objects.assign(Page<E>, {...json, data});
    }


    
}


export interface Pageable {
    offset?: number;
   // limit?: number;
    sortBy?: string;
    size?: number;
    page?: number;
}