import {
    AbstractControl,
    AbstractControlOptions,
    ControlConfig,
    FormBuilder, FormControl,
    FormControlOptions, FormControlState,
    FormGroup,
    ɵElement
} from "@angular/forms";
import {Injectable} from "@angular/core";
import {CustomFormGroup} from "./form-group";

type ControlOption = AbstractControlOptions | {[key: string]: any} | null | undefined;

function isAbstractControlOptions(options: ControlOption): options is AbstractControlOptions {
    return (!!options &&
        ((options as AbstractControlOptions).asyncValidators !== undefined ||
            (options as AbstractControlOptions).validators !== undefined ||
            (options as AbstractControlOptions).updateOn !== undefined)
    );
}


@Injectable
export class FormsBuilder extends FormBuilder {

    override group<E>(controls: T, options?: ControlOption): CustomFormGroup<E> {
        const reducedControls = this._reduceControls(controls);
        let newOptions: FormControlOptions = {};
        if (isAbstractControlOptions(options)) {
            // `options` are `AbstractControlOptions`
            newOptions = options;
        } else if (options !== null) {
            // `options` are legacy form group options
            newOptions.validators = (options as any).validator;
            newOptions.asyncValidators = (options as any).asyncValidator;
        }
        return new CustomFormGroup(reducedControls, newOptions);
    }


    /** @internal */
    private _reduceControls<T>(controls: { [k: string]: T | ControlConfig<T> | FormControlState<T> | AbstractControl<T>; }): {[key: string]: AbstractControl} {
        const createdControls: {[key: string]: AbstractControl} = {};
        Object.keys(controls).forEach((controlName) => {
            createdControls[controlName] = this._createControl(controls[controlName]);
        });
        return createdControls;
    }

    /** @internal */
    private _createControl<T>(controls: T | FormControlState<T> | ControlConfig<T> | FormControl<T> | AbstractControl<T>): FormControl<T> | FormControl<T | null> | AbstractControl<T> {
        if (controls instanceof FormControl) {return controls as FormControl<T>;}
        else if (controls instanceof AbstractControl) {return controls;}
        else if (Array.isArray(controls)) {
            // ControlConfig Tuple
            const value: T | FormControlState<T> = controls[0];
            const validator: ValidatorFn | ValidatorFn[] | null = controls.length > 1 ? controls[1]! : null;
            const asyncValidator: AsyncValidatorFn | AsyncValidatorFn[] | null = controls.length > 2 ? controls[2]! : null;
            return this.control<T>(value, validator, asyncValidator);
        } else {
            // T or FormControlState<T>
            return this.control<T>(controls);
        }
    }

}