import { FormGroup } from "@angular/forms";
import { Consumer } from "ts-ui/helper";

export class FormsUtil {

    constructor(private form: FormGroup) { }

    subscribeControl(control: string, callback: Consumer<any>): void {
        this.form.get(control).valueChanges.subscribe({ next: data => callback(data) });
    }

    setControlValue(control: string, value: any) {
        this.form.get(control).setValue(value);
    }

    setPathControlValue(control: string, value: any) {
        this.form.get(control).patchValue(value);
    }

    getControlValue<E>(control: string): E {
        return this.form.get(control).value;
    }
}