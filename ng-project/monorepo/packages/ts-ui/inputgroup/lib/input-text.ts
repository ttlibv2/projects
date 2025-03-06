import { DOCUMENT } from "@angular/common";
import { AfterContentInit, booleanAttribute, ChangeDetectionStrategy, ChangeDetectorRef, Component, ContentChildren, ElementRef, EventEmitter, forwardRef, Inject, Injector, Input, numberAttribute, Output, QueryList, TemplateRef, ViewChild, ViewEncapsulation } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import {  PrimeTemplate } from "primeng/api";
import { PrimeNG } from "primeng/config";

export const INPUTTEXT_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputText),
    multi: true
};

@Component({
    selector: 'ts-input',
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    providers: [INPUTTEXT_VALUE_ACCESSOR],
    templateUrl: './input-text.html'
})
export class InputText implements AfterContentInit, ControlValueAccessor {

    /**
     * When enabled, a clear icon is displayed to clear the value.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) showClear: boolean = false;

    /**
     * When present, it specifies that the component should automatically get focus on load.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) autofocus: boolean | undefined;

    @Input() clearIcon?: string;

    /**
     * Identifier of the focus input to match a label defined for the component.
     * @group Props
     */
    @Input() inputId: string | undefined;

    /**
     * Style class of the component.
     * @group Props
     */
    @Input() styleClass: string | undefined;

    /**
     * Inline style of the component.
     * @group Props
     */
    @Input() style: { [klass: string]: any } | null | undefined;

    /**
     * Advisory information to display on input.
     * @group Props
     */
    @Input() placeholder: string | undefined;

    /**
     * Maximum number of character allows in the input field.
     * @group Props
     */
    @Input({ transform: numberAttribute }) maxlength: number | undefined;

    /**
     * Specifies one or more IDs in the DOM that labels the input field.
     * @group Props
     */
    @Input() ariaLabelledBy: string | undefined;

    /**
     * Used to define a string that labels the input element.
     * @group Props
     */
    @Input() ariaLabel: string | undefined;
    /**
     * Used to indicate that user input is required on an element before a form can be submitted.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) ariaRequired: boolean | undefined;

    /**
     * Specifies tab order of the element.
     * @group Props
     */
    @Input({ transform: numberAttribute }) tabindex: number | undefined;

    /**
     * Name of the input field.
     * @group Props
     */
    @Input() name: string | undefined;

    /**
     * Indicates that whether the input field is required.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) required: boolean | undefined;

    /**
     * Used to define a string that autocomplete attribute the current element.
     * @group Props
     */
    @Input() autocomplete: string | undefined;

    /**
     * When present, it specifies that an input field is read-only.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) readonly: boolean = false;

    /**
     * Text to display after the value.
     * @group Props
     */
    @Input() suffix: string | undefined;

    /**
     * Text to display before the value.
     * @group Props
     */
    @Input() prefix: string | undefined;

    /**
     * Callback to invoke when clear token is clicked.
     * @group Emits
     */
    @Output() onClear = new EventEmitter<any>();
    @Output() onInput = new EventEmitter<any>();
    @Output() onKeyDown = new EventEmitter<any>();
    @Output() onKeyPress = new EventEmitter<any>();
    @Output() onClick = new EventEmitter<any>();
    @Output() onPaste = new EventEmitter<any>();
    @Output() onFocus = new EventEmitter<any>();
    @Output() onBlur = new EventEmitter<any>();

    @ViewChild('input') input!: ElementRef<HTMLInputElement>;


    @ContentChildren(PrimeTemplate)
    templates!: QueryList<PrimeTemplate>;

    clearIconTemplate: TemplateRef<any>;

    value: string;
    disabled: boolean;
    private onModelChange: Function = () => { };
    private onModelTouched: Function = () => { };


    constructor(
        @Inject(DOCUMENT) private document: Document,
        public el: ElementRef,
        private cd: ChangeDetectorRef,
        private readonly injector: Injector,
        public config: PrimeNG) { }

    ngAfterContentInit() {
        this.templates.forEach((item) => {
            switch (item.getType()) {
                case 'clearicon':
                    this.clearIconTemplate = item.template;
                    break;
            }
        });
    }


    writeValue(value: any): void {
        this.value = value;
        this.cd.markForCheck();
    }

    registerOnChange(fn: Function): void {
        this.onModelChange = fn;
    }

    registerOnTouched(fn: Function): void {
        this.onModelTouched = fn;
    }

    setDisabledState?(isDisabled: boolean): void {
        this.disabled = isDisabled;
        this.cd.markForCheck();
    }

    onUserInput(event: any): void {
        this.value = (event.target as HTMLInputElement).value;
        this.onInput.emit(event);
        this.onModelChange(this.value);
    }

    onInputKeyDown(event: any): void {
        this.onKeyDown.emit(event);
    }

    onInputKeyPress(event: any): void {
        this.onKeyPress.emit(event);
    }

    onInputPaste(event: any): void {
        this.onPaste.emit(event);
    }

    onInputClick(event: any): void {
        this.onClick.emit(event);
    }

    onInputFocus(event: any): void {
        this.onFocus.emit(event);
    }

    onInputBlur(event: any): void {
        this.onBlur.emit(event);
    }

}