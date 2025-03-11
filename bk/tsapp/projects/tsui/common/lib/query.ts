import { QueryList } from "@angular/core";
import { Subscription, Subject, takeUntil } from "rxjs";
import { Asserts, BiConsumer, Consumer } from "ts-ui/helper";

export abstract class QueryUtil {
    private constructor() {}

    static queryList<T>(destroy$: Subject<boolean>, list: QueryList<T>, beforeRun: Consumer<void>, consumer: BiConsumer<T, number>): Subscription {
        Asserts.notNull(list, "@list is null");

        const applyQueryList = (l: QueryList<T>) => {
            if(!!beforeRun) beforeRun();
            if(!!l)l.forEach((item, index) => consumer(item, index));
        };

        // apply first
        applyQueryList(list);
        return list.changes.pipe(takeUntil(destroy$))
            .subscribe((ts: QueryList<T>) => applyQueryList(ts));        
    }

}