import { catchError, concatMap, delay, Observable, Observer, of } from "rxjs";
import { Objects } from "ts-ui/helper";

function callFunc(func: any, data?: any) {
    if(Objects.notNull(func)) {
        if(Objects.notNull(data)) func(data);
        else func();
    }
}

export class RxjsUtil {

    static runConcatMap(lsObs: Observable<any>[], delayTime: number, stopErr: boolean = true, observerOrNext?: Partial<Observer<any>>)  {
        const mpObs = of(...lsObs).pipe(concatMap(obs => obs.pipe(delay(delayTime))));
        const runObs =  mpObs.subscribe({
            next: data => callFunc(observerOrNext?.next, data),
            complete:() => callFunc(observerOrNext?.complete ),
            error: err => {
                callFunc(observerOrNext?.error, err);
                if(stopErr === true) runObs.unsubscribe();            
            },
           
        });




        
    }
}