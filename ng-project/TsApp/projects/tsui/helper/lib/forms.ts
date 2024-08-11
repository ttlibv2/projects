import {AbstractControlOptions, FormBuilder, FormControl, FormGroup, ValidationErrors} from "@angular/forms";
import {BiConsumer, Consumer} from "./function";
import {Objects} from "./objects";

export interface ValueOption {
    onlySelf?: boolean;
    emitEvent?: boolean;
}

export type TControl<E> = {[K in keyof E]?: any};
export type ControlKey<E> = string & keyof TControl<E>;

export class Forms<E=any> {

    static builder<E>(fb: FormBuilder, controls: TControl<E>, options?:AbstractControlOptions): Forms<E> {
        return new Forms<E>(fb, <FormGroup>fb.group(controls, options));
    }

    private constructor(private readonly fb: FormBuilder,
                private readonly delegate: FormGroup<TControl<E>>) {
    }

    get formGroup(): FormGroup {
        return this.delegate;
    }

    get formRawValue(): Partial<E> {
        return <any>this.delegate.getRawValue();
    }

    get invalid(): boolean {
        return this.formGroup.invalid;
    }
    
    formValueChange(consumer: Consumer<Partial<E>>):void {
        this.delegate.valueChanges.subscribe(value => consumer(<any>value));
    }

    controlValueChange<C extends ControlKey<E>>(control: C, consumer: Consumer<E[C]>):void {
        this.getControl(control).valueChanges.subscribe(value => consumer(value));
    }

    controlsValueChange<C extends ControlKey<E>>(controls: C[], consumer: BiConsumer<Forms<E>, any>):void {
        const json = () => Objects.arrayToJson(controls, c => [c, this.getControl(c).getRawValue()]);
        controls.map(c => this.getControl(c)).forEach(c => c.valueChanges.subscribe(_ => consumer(this, json())))
    }

    getControl<C extends ControlKey<E>>(name: C | C[]) {
        return this.delegate.get(name);
    }

    pathValue(values: Partial<E>, options?: ValueOption) {
        this.delegate.patchValue(<any>values, options);
    }

    pathValueControl<C extends ControlKey<E>>(name: C, value: E[C], options?: ValueOption) {
        return this.delegate.get(name).patchValue(<any>value, options);
    }

    setValueControl<C extends ControlKey<E>>(name: C, value: E[C], options?: ValueOption) {
        return this.delegate.get(name).setValue(<any>value, options);
    }

    resetForm(value?: Partial<E>, options?: ValueOption) {
        this.delegate.reset(<any>value, options)
    }

    setControlError<C extends ControlKey<E>>(control: C, errors: ValidationErrors) {
        this.getControl(control).setErrors(errors, {emitEvent: true});
    }

    setDisableControl<C extends ControlKey<E>>(disabled: boolean, controls: C[]) {
        controls.map(c => this.getControl(c)).forEach(c => disabled ? c.disable() : c.enable());
    }
}