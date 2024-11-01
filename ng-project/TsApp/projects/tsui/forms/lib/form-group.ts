import { AbstractControl, FormGroup as NgxFormGroup, ValidationErrors, ɵFormGroupRawValue, ɵGetProperty, ɵTypedOrUntyped } from "@angular/forms";
import { AsyncValidator, ValidatorOrOpts } from "./forms.common";
import { BiConsumer, Consumer, Objects } from "ts-ui/helper";

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

    controlsValueChange<K extends TKeyControl<TC>>(controls: K[], consumer: BiConsumer<FormGroup, TRawValue<TC>>): void {
        const func =  () => <any>Objects.arrayToJson(controls, c => [c, this.get_value(c)]);
        controls.forEach(c => this.get(c).valueChanges.subscribe(_ => consumer(this, func())));
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

    /**
     * Sets errors on a form control when running validations manually, rather than automatically.
     * @param opts Configuration options that determine how the control propagates
     * changes and emits events after the control errors are set.
     * * `emitEvent`: When true or not supplied (the default), the `statusChanges`
     * observable emits an event after the errors are set.
     */
    set_error<K extends TKeyControl<TC>>(name: K, errors: ValidationErrors | null, opts?: { emitEvent?: boolean; }): void {
        this.get(name).setErrors(errors, opts);
    }

    set_value<K extends TKeyControl<TC>>(name: K, value: any) {
        this.get(name).setValue(value);
    }

    reset_value<K extends TKeyControl<TC>>(name: K, value: any) {
        this.get(name).reset(value);
    }

    /**
     * The raw value of this control. For most control implementations, the raw value will include
     * disabled children.
     */
    get_value<K extends TKeyControl<TC>>(name: K) {
        return this.get(name).getRawValue();
    }

    /**
     * Disables the control. This means the control is exempt from validation checks and
     * excluded from the aggregate value of any parent. Its status is `DISABLED`.
     *
     * If the control has children, all children are also disabled.
     *
     * @see {@link AbstractControl.status}
     *
     * @param opts Configuration options that determine how the control propagates
     * changes and emits events after the control is disabled.
     * * `onlySelf`: When true, mark only this control. When false or not supplied,
     * marks all direct ancestors. Default is false.
     * * `emitEvent`: When true or not supplied (the default), the `statusChanges`,
     * `valueChanges` and `events`
     * observables emit events with the latest status and value when the control is disabled.
     * When false, no events are emitted.
     */
    set_disable<K extends TKeyControl<TC>>(name: K,opts?: {onlySelf?: boolean; emitEvent?: boolean; }) {
        this.get(name).disable(opts);
    }

    /**
     * Patches the value of the control
     * @param control the control name
     * @param value the value update
     * @param options the option
     * 
     */
    patchControl<K extends TKeyControl<TC>>(control: K, value: any, options?: Options): void {
        this.get(control).patchValue(value, options);
    }


}