import { AbstractControlOptions, FormBuilder, FormGroup } from "@angular/forms";
import { JsonAny } from "./common";
import { Objects } from "./objects";
const {notEmpty} = Objects;

export interface ValueOption {
    onlySelf?: boolean;
    emitEvent?: boolean;
}

export class FormUtil<E=any> {

    static create<E=any>(builder: FormBuilder, controls?: JsonAny, options?: AbstractControlOptions): FormUtil<E> {
        const forms = new FormUtil(builder).group(controls, options);
        if(notEmpty(controls)) forms.group(controls, options);
        return forms;
    }

    fg: FormGroup = undefined;

    constructor(private builder: FormBuilder) {}

    group(controls: JsonAny, options?: AbstractControlOptions): this {
        this.fg = this.builder.group(controls, options);
        return this;
    }

    pathValue(value: Partial<E>, options?: ValueOption): this {
        this.fg.patchValue(value, options);
        return this;
    }



}