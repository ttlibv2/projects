import { FormGroup } from "@angular/forms";
import { Asserts, Consumer, Supplier } from "ts-ui/helper";

export interface ValueOption {
    onlySelf?: boolean;
    emitEvent?: boolean
}


export class FormUtil {


    static create(supiler: Supplier<FormGroup>, initForm?: Consumer<FormUtil>) {
        const form = new FormUtil(supiler());
        form.initialize(initForm);
        return form;
    }

    private constructor(public readonly fg: FormGroup) { }

    private initialize(initForm: Consumer<FormUtil>): this {
        Asserts.notNull(initForm, "@Consumer<FormGroup>");
        initForm(this);
        return this;
    }

    private get(controlKey: string) {
        return this.fg.get(controlKey);
    }

    formValueChange<V = any>(consumer: Consumer<V>) {
        this.fg.valueChanges.subscribe(value => consumer(value));
    }

    controlValueChange<V = any>(controlKey: string, consumer: Consumer<V>) {
        this.fg.get(controlKey).valueChanges.subscribe(value => consumer(value));
    }

    pathControl(controlKey: string, value: any, option?: ValueOption) {
        this.fg.get(controlKey).patchValue(value, option);
    }


    rawValue(): any {
        return { ...this.fg.getRawValue() };
    }

}