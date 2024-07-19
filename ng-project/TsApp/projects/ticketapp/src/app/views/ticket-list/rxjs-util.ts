import { catchError, concatMap, delay, Observable, Observer, of } from "rxjs";

export class RxjsUtil {

    static runConcatMap(lsObs: Observable<any>[], delayTime: number, stopErr: boolean = true, observerOrNext?: Partial<Observer<any>>)  {
        const mpObs = of(...lsObs).pipe(concatMap(obs => obs.pipe(delay(delayTime))));
        const runObs =  mpObs.subscribe({
            next: data => observerOrNext?.next(data),
            complete:() => observerOrNext?.complete(),
            error: err => {
                observerOrNext?.error(err);
                if(stopErr === true) runObs.unsubscribe();            
            },
           
        })
    }
}