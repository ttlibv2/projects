import { QueryList } from "@angular/core";
import { Subscription } from "rxjs";
import { Asserts, BiConsumer, Consumer, Objects } from "ts-ui/helper";

export abstract class QueryUtil {
    private constructor() {}

    static queryList<T>(list: QueryList<T>, beforeRun: Consumer<void>, consumer: BiConsumer<T, number>): Subscription {
        Asserts.notNull(list, "@list is null");

        const applyQueryList = (l: QueryList<T>) => {
            if(!!beforeRun) beforeRun();
            if(!!l)l.forEach((item, index) => consumer(item, index));
        };

        // apply first
        applyQueryList(list);
        return list.changes.subscribe((ts: QueryList<T>) => applyQueryList(ts));        
    }

}