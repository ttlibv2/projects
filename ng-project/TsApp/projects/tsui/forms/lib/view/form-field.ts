import { CommonModule } from "@angular/common";
import { Component, Input, ViewEncapsulation } from "@angular/core";

@Component({
    selector: 'ts-form-field',
    standalone: true,
    imports: [CommonModule],
    encapsulation: ViewEncapsulation.None,
    templateUrl: './form-field.html'
})
export class FormField {
    @Input() inputId: string;
}