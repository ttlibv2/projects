import { AbstractControl, FormGroup as NgxFormGroup, ɵFormGroupRawValue, ɵGetProperty, ɵTypedOrUntyped } from "@angular/forms";
import { AsyncValidator, ValidatorOrOpts } from "./forms.common";
import { Consumer } from "ts-ui/helper";

export type TControl<T> = { [K in keyof T]: AbstractControl<any, any> };
export type TKeyControl<TC> = string & keyof TC;
export type TRawValue<TC extends TControl<TC>> = ɵTypedOrUntyped<TC, ɵFormGroupRawValue<TC>, any>;
export type TControlValue<TC extends TControl<TC>, K extends TKeyControl<TC>> = ɵGetProperty<TRawValue<TC>, K>;





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

    registerEvent(consumer: Consumer<this>): void {
        consumer(this);
    }

    formValueChange(valueCb: Consumer<TRawValue<TC>>): void {
        this.valueChanges.subscribe((value: any) => valueCb(value));
    }

    controlValueChange<K extends TKeyControl<TC>>(control: K, valueCb: Consumer<TControlValue<TC, K>>): void {
        this.get(control).valueChanges.subscribe((value: any) => valueCb(value));
    }

    enableOrDisableControl<K extends TKeyControl<TC>>(control: K, enable: boolean):void {
        enable ? this.get(control)?.enable() : this.get(control)?.disable();
    }










    override getRawValue(): ɵTypedOrUntyped<TC, ɵFormGroupRawValue<TC>, any> {
        return super.getRawValue();
    }

    /**
     * Retrieves a child control given the control's name or path.
     *
     * This signature for get supports strings and `const` arrays (`.get(['foo', 'bar'] as const)`).
     */
    override get<K extends TKeyControl<TC>>(control: K): AbstractControl<TControlValue<TC, K>> {
        return super.get(control);
    }

    get_value<K extends TKeyControl<TC>>(name: K) {
        return this.get(name).getRawValue();
    }


    /**
     * Patches the value of the control
     * @param control the control name
     * @param value the value update
     * @param options the option
     * 
     */
    pathControl<K extends TKeyControl<TC>>(control: K, value: any, options?: Options): void {
        this.get(control).patchValue(value, options);
    }





}