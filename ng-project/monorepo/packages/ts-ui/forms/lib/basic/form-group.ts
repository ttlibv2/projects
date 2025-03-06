import { AbstractControl, FormGroup as NgxFormGroup, ValidationErrors, ɵFormGroupRawValue, ɵGetProperty, ɵTypedOrUntyped } from "@angular/forms";
import { AsyncValidator, ValidatorOrOpts } from "./form-common";
import { BiConsumer, Consumer, Objects } from "ts-ui/helper";
import { debounceTime, distinctUntilChanged, filter, Subject, takeUntil } from "rxjs";

export type TControl<T> = { [K in keyof T]: AbstractControl<any, any> };
export type TKC<TC> = string & keyof TC;
export type TRValue<TC extends TControl<TC>> = ɵTypedOrUntyped<TC, ɵFormGroupRawValue<TC>, any>;
export type TCValue<TC extends TControl<TC>, K extends TKC<TC>> = ɵGetProperty<TRValue<TC>, K>;

export interface Options {
    onlySelf?: boolean;
    emitEvent?: boolean;
}

export class FormGroup<TC extends TControl<TC> = any> extends NgxFormGroup<TC> {

    /**
     * Creates a new `FormGroup` instance.
     *
     * @param controls A collection of child controls. The key for each child is the name
     * under which it is registered.
     *
     * @param validatorOrOpts A synchronous validator function, or an array of
     * such functions, or an `AbstractControlOptions` object that contains validation functions
     * and a validation trigger.
     *
     * @param asyncValidator A single async validator or array of async validator functions
     *
     * */
    constructor(controls: TC, validatorOrOpts?: ValidatorOrOpts, asyncValidator?: AsyncValidator | null) {
        super(controls, validatorOrOpts, asyncValidator);
    }

    formChange(valueCb: Consumer<TRValue<TC>>): void {
        this.valueChanges.subscribe((_: any) => valueCb(this.getRawValue()));
    }

    distinctChange(destroy$: Subject<any>, options: {
        filter: (fb: FormGroup) => boolean,
        debounceTime: number,
        comparator?: (previous: any, current: any) => boolean,
        subscribe: Consumer<TRValue<TC>>
    }): void {

        const predicate = () => options?.filter(this) ?? true;
        const comparator = (v1: any, v2: any) => options?.comparator(v1, v2) ?? v1 === v2;
        const dueTime = options?.debounceTime ?? 200;
        const subscribe = options?.subscribe ?? ((val: any) =>  {});

        const pipe$ = this.valueChanges.pipe(
            filter(() => predicate()),
            debounceTime(dueTime),
            distinctUntilChanged((prev: any, current) =>Object.keys(prev).every(key => comparator(prev[key],current[key]))),
            takeUntil(destroy$)
        );

        pipe$.subscribe(value => subscribe(value))
    }











    controlChange<K extends TKC<TC>>(control: K, valueCb: BiConsumer<AbstractControl<TCValue<TC, K>>, TCValue<TC, K>>): void {
        const contr = this.get(control);
        contr.valueChanges.subscribe((value: any) => valueCb(contr, value));
    }

    controlsChange<K extends TKC<TC>>(controls: K[], consumer: BiConsumer<FormGroup, TRValue<TC>>): void {
        const func =  () => <any>Objects.arrayToJson(controls, c => [c, this.get_value(c)]);
        controls.forEach(c => this.get(c).valueChanges.subscribe(_ => consumer(this, func())));
    }

    override getRawValue(): ɵTypedOrUntyped<TC, ɵFormGroupRawValue<TC>, any> {
        return super.getRawValue();
    }

    /**
     * Retrieves a child control given the control's name or path.
     *
     * This signature for get supports strings and `const` arrays (`.get(['foo', 'bar'] as const)`).
     */
    override get<K extends TKC<TC>>(control: K | string[]): AbstractControl<TCValue<TC, K>> {
        return super.get(control);
    }

    set_enable<K extends TKC<TC>>(control: K, flag: boolean, options?: Options): void {
        this.get(control)[flag ? 'enable' : 'disable'](options);
    }

    /**
     * Sets errors on a form control when running validations manually, rather than automatically.
     * @param name
     * @param errors
     * @param opts Configuration options that determine how the control propagates
     * changes and emits events after the control errors are set.
     * * `emitEvent`: When true or not supplied (the default), the `statusChanges`
     * observable emits an event after the errors are set.
     */
    set_error<K extends TKC<TC>>(name: K, errors: ValidationErrors | null, opts?: { emitEvent?: boolean; }): void {
        this.get(name).setErrors(errors, opts);
    }

    set_value<K extends TKC<TC>>(name: K, value: any) {
        this.get(name).setValue(value);
    }

    reset_value<K extends TKC<TC>>(name: K, value: any) {
        this.get(name).reset(value);
    }

    path_value<K extends TKC<TC>>(name: K, value: any, options?: Options) {
        this.get(name).patchValue(value, options);
    }

    /**
     * The raw value of this control. For most control implementations, the raw value will include
     * disabled children.
     */
    get_value<K extends TKC<TC>>(name: K) {
        return this.get(name).getRawValue();
    }

}