import {AbstractControl, AbstractControlOptions, AsyncValidatorFn, FormControl, ValidatorFn} from "@angular/forms";
import {CustomFormGroup} from "./form-group";

export type FieldLayout = 'horizontal' | 'vertical';
export type TControl<E> = {[K in keyof E]?: AbstractControl<any> };
export type ValidatorOrOpts = ValidatorFn | ValidatorFn[] | AbstractControlOptions | null;
export type AsyncValidator = AsyncValidatorFn | AsyncValidatorFn[] | null;
export type RawValue<C> = C extends AbstractControl<any, any> ? C['setValue'] extends (v: infer R) => void ? R : never : never

export interface ControlValueOption {
    onlySelf?: boolean;
    emitEvent?: boolean;
    emitModelToViewChange?: boolean;
    emitViewToModelChange?: boolean;
}


interface User {
    userId: number;
    username: string;
    password: string;
    email: string;
}


let group = new CustomFormGroup<User>({
    userId: new FormControl(),
    username: new FormControl(),
    password: new FormControl(),
    email: new FormControl()
});

group.set('userId', 1233333);