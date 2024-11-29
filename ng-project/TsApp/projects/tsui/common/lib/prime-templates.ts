import { QueryList, TemplateRef } from "@angular/core";
import { PrimeTemplate } from "primeng/api";
import { Subject, takeUntil } from "rxjs";
import { Asserts, Consumer, JsonAny, Objects } from "ts-ui/helper";

export type TemplateItem = JsonAny<TemplateRef<any>>;

interface Options {
    onClear?: Consumer<void>;
    onUpdate: Consumer<TemplateItem>;
    templates: QueryList<PrimeTemplate>;
}

export class QueryPTemplate {

    static register(options: Options) {
        Asserts.notNull(options.templates, "@QueryList<PrimeTemplate>");
        const cls = new QueryPTemplate(options.templates);

        if(!!options && !!options.onUpdate){
            cls.onClear = options?.onClear;
            cls.onUpdate = options?.onUpdate;
            cls.initialize();
        }

        return cls;
    }

    private _destroy$ = new Subject<boolean>();
    private _onClear: Consumer<void>;
    private _onUpdate: Consumer<TemplateItem>;
    private _hasinitialize: boolean = false;

    constructor(private readonly templates: QueryList<PrimeTemplate>) {
    }

    /**
     * Returns destroy subject
     * */
    get destroy$() {
        return this._destroy$;
    }

    /**
     * set clear function
     * */
    set onClear(func: Consumer<void>) {
        this._onClear = func;
    }

    /**
     * set update function
     * */
    set onUpdate(func: Consumer<TemplateItem>) {
        this._onUpdate = func;
    }

    initialize(): void {
        if (this._hasinitialize === false) {
            this._hasinitialize = true;
            this.showWarning();
            this.extractTemplates(this.templates, true);
            this.templates.changes.pipe(takeUntil(this._destroy$))
                .subscribe(list => this.extractTemplates(list));
        }
    }

    destroy(): void {
        this._destroy$.next(true);
        this._destroy$.complete();
        this.callClear();
        this._onClear = undefined;
        this._onUpdate = undefined;
    }

    private callClear(): void {
        if (this._onClear) this._onClear();
    }

    private callUpdate(object: any): void {
        if (this._onUpdate) this._onUpdate(object);
    }

    private extractTemplates(templates: QueryList<PrimeTemplate>, first: boolean = false): void {
        if (first === false) this.callClear();
        this.callUpdate(Objects.arrayToJson([...templates], item => [item.getType(), item.template]));
    }

    private showWarning(): void {
        if (this._onClear === undefined) console.warn(`function onClear not setting !!`);
        if (this._onUpdate === undefined) console.warn(`function onUpdate not setting !!`);
    }
}