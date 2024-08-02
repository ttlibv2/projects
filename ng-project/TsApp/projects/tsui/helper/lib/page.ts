import {Objects} from "./objects";

export class Page<E> {
    data: E[];
    offset: number = 0;
    limit: number = 1000;
    total: number;
    currentPage: number = 1;
    sortBy: string;

    set_data(data: any[]) {
        this.data = data;
        return this;
    }

    set_total(total: number) {
        this.total = total;
        return this;
    }

    static from<E>(pageable: Pageable): Page<E> {
        return Objects.assign(Page<E>, pageable);
    }
}


export interface Pageable {
    offset?: number;
    limit?: number;
    sortBy?: string;
}