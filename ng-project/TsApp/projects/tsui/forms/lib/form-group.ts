import {FormGroup} from "@angular/forms";
import {ControlValueOption, RawValue, TControl} from "./interface";

type KC<TC> = string & keyof TC;

export class CustomFormGroup<E=any, TC extends TControl<E> = any> extends FormGroup<TC> {

    set<K extends KC<TC>>(control: K, value: RawValue<TC[K]>, options?: ControlValueOption) {
        super.get(control).setValue(<any>value, options);
    }

    


}