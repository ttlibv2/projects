import { AbstractControl, AbstractControlOptions, FormControlOptions } from "@angular/forms";
import { AsyncValidatorFn, ValidatorFn } from "@angular/forms";


export type EControl<E> = {[K in keyof E]: AbstractControl<any>};
export type ValidatorOrOpts = ValidatorFn | ValidatorFn[] | AbstractControlOptions | null;
export type CValidatorOrOpts = ValidatorFn | ValidatorFn[] | FormControlOptions | null;

export type AsyncValidator = AsyncValidatorFn | AsyncValidatorFn[];