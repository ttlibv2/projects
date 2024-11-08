import { CommonModule } from "@angular/common";
import { booleanAttribute, ChangeDetectorRef, Component, EventEmitter, forwardRef, Input, numberAttribute, OnDestroy, Output, ViewEncapsulation } from "@angular/core";
import { ColorPickerChangeEvent, ColorFormatType } from "./interface";
import { Subject } from "rxjs";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { FormsBuilder, FormsModule } from "ts-ui/forms";

@Component({
    standalone: true,
    selector: 'ts-colorpicker',
    encapsulation: ViewEncapsulation.None,
    templateUrl: './picker.html',
    imports: [CommonModule],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => ColorPicker),
            multi: true
        }
    ]
})
export class ColorPicker implements ControlValueAccessor, OnDestroy {

    /**
    * Inline style of the component.
    * @group Props
    */
    @Input() style: { [klass: string]: any } | null | undefined;

    /**
     * Style class of the component.
     * @group Props
     */
    @Input() styleClass: string | undefined;

    /**
     * Format to use in value binding.
     * @group Props
     */
    @Input() format: ColorFormatType = 'hex';

    /**
     * When present, it specifies that the component should be disabled.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) disabled: boolean | undefined;

    /**
     * Index of the element in tabbing order.
     * @group Props
     */
    @Input() tabindex: string | undefined;

    /**
     * Whether to display as an overlay or not.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) inline: boolean | undefined;

    /**
     * Identifier of the focus input to match a label defined for the dropdown.
     * @group Props
     */
    @Input() inputId: string | undefined;

    /**
     * Whether to automatically manage layering.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) autoZIndex: boolean = true;

    /**
     * Base zIndex value to use in layering.
     * @group Props
     */
    @Input({ transform: numberAttribute }) baseZIndex: number = 0;

    /**
     * Transition options of the show animation.
     * @group Props
     */
    @Input() showTransitionOptions: string = '.12s cubic-bezier(0, 0, 0.2, 1)';

    /**
     * Transition options of the hide animation.
     * @group Props
     */
    @Input() hideTransitionOptions: string = '.1s linear';

    /**
     * When present, it specifies that the component should automatically get focus on load.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) autofocus: boolean | undefined;

    @Input({ transform: booleanAttribute }) showInput: boolean | undefined;

    @Input() defaultValue: string | object;

    /**
     * Callback to invoke on value change.
     * @param {ColorPickerChangeEvent} event - Custom value change event.
     * @group Emits
     */
    @Output() onChange = new EventEmitter<ColorPickerChangeEvent>();

    @Output() onFormatChange = new EventEmitter<ColorFormatType>();

    /**
     * Callback to invoke on panel is shown.
     * @group Emits
     */
    @Output() onShow = new EventEmitter<any>();

    /**
     * Callback to invoke on panel is hidden.
     * @group Emits
     */
    @Output() onHide = new EventEmitter<any>();


    private destroy$ = new Subject<void>();
    private _onChange: (value: string) => void = () => { };

    constructor(
        private formBuilder: FormsBuilder,
        private cdr: ChangeDetectorRef
    ) { }

    writeValue(obj: any): void {
        throw new Error("Method not implemented.");
    }

    registerOnChange(fn: any): void {
        this._onChange = fn;
    }

    registerOnTouched(fn: any): void {
    }

    setDisabledState?(isDisabled: boolean): void {
        this.disabled = isDisabled;
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

}