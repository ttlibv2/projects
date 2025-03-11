import { Injectable } from "@angular/core";
import { FormGroup } from "./form-group";
import { AbstractControl, AbstractControlOptions, AsyncValidatorFn, 
    ControlConfig, FormBuilder as FormBuilder2, FormControl, FormControlOptions, 
    FormControlState, ValidatorFn, ɵElement } from "@angular/forms";


function isAbstractControlOptions(
    options: AbstractControlOptions | { [key: string]: any } | null | undefined,
): options is AbstractControlOptions {
    return (
        !!options &&
        ((options as AbstractControlOptions).asyncValidators !== undefined ||
            (options as AbstractControlOptions).validators !== undefined ||
            (options as AbstractControlOptions).updateOn !== undefined)
    );
}

type ControlType<T> = T | FormControlState<T> | ControlConfig<T> | FormControl<T> | AbstractControl<T>;

@Injectable({providedIn: 'root'})
export class FormsBuilder extends FormBuilder2 {

    /**
    * @description
    * Constructs a new `FormGroup` instance. Accepts a single generic argument, which is an object
    * containing all the keys and corresponding inner control types.
    *
    * @param controls A collection of child controls. The key for each child is the name
    * under which it is registered.
    *
    * @param options Configuration options object for the `FormGroup`. The object should have the
    * `AbstractControlOptions` type and might contain the following fields:
    * * `validators`: A synchronous validator function, or an array of validator functions.
    * * `asyncValidators`: A single async validator or array of async validator functions.
    * * `updateOn`: The event upon which the control should be updated (options: 'change' | 'blur'
    * | submit').
    */
    override group<T extends {}>(controls: T, options?: AbstractControlOptions | null): FormGroup<{ [K in keyof T]: ɵElement<T[K], null>; }>;

    /**
     * @description
     * Constructs a new `FormGroup` instance.
     *
     * @deprecated This API is not typesafe and can result in issues with Closure Compiler renaming.
     * Use the `FormBuilder#group` overload with `AbstractControlOptions` instead.
     * Note that `AbstractControlOptions` expects `validators` and `asyncValidators` to be valid
     * validators. If you have custom validators, make sure their validation function parameter is
     * `AbstractControl` and not a sub-class, such as `FormGroup`. These functions will be called
     * with an object of type `AbstractControl` and that cannot be automatically downcast to a
     * subclass, so TypeScript sees this as an error. For example, change the `(group: FormGroup) =>
     * ValidationErrors|null` signature to be `(group: AbstractControl) => ValidationErrors|null`.
     *
     * @param controls A record of child controls. The key for each child is the name
     * under which the control is registered.
     *
     * @param options Configuration options object for the `FormGroup`. The legacy configuration
     * object consists of:
     * * `validator`: A synchronous validator function, or an array of validator functions.
     * * `asyncValidator`: A single async validator or array of async validator functions
     * Note: the legacy format is deprecated and might be removed in one of the next major versions
     * of Angular.
     */
    override group(controls: { [key: string]: any; }, options: { [key: string]: any; }): FormGroup;


    /**@internal*/
    override group(
        controls: { [key: string]: any },
        options: AbstractControlOptions | { [key: string]: any } | null = null
    ): FormGroup {
        const reducedControls = this.reduceControls(controls);
        let newOptions: FormControlOptions = {};
        if (isAbstractControlOptions(options)) {
            // `options` are `AbstractControlOptions`
            newOptions = options;
        } else if (options !== null) {
            // `options` are legacy form group options
            newOptions.validators = (options as any).validator;
            newOptions.asyncValidators = (options as any).asyncValidator;
        }
        return new FormGroup(reducedControls, newOptions);
    }


    /** @internal */
    reduceControls<T>(controls: { [k: string]: ControlType<T>; }): any {
        const createdControls: { [k:string]: AbstractControl } = {};
        Object.keys(controls).forEach((controlName) => {
            createdControls[controlName] = this.createControl(controls[controlName]);
        });
        return createdControls;
    }

    /** @internal */
    createControl<T>(controls: ControlType<T>): FormControl<T> | FormControl<T | null> | AbstractControl<T> {
        if (controls instanceof FormControl) {
            return controls as FormControl<T>;
        } ///
        else if (controls instanceof AbstractControl) {
            // A control; just return it
            return controls;
        } //
        else if (Array.isArray(controls)) {
            // ControlConfig Tuple
            const value: T | FormControlState<T> = controls[0];
            const validator: ValidatorFn | ValidatorFn[] | null = controls.length > 1 ? controls[1]! : null;
            const asyncValidator: AsyncValidatorFn | AsyncValidatorFn[] | null = controls.length > 2 ? controls[2]! : null;
            return this.control<T>(value, validator, asyncValidator);
        } //
        else {
            // T or FormControlState<T>
            return this.control<T>(controls);
        }
    }

}