import { booleanAttribute,  Component, ElementRef, EventEmitter, forwardRef, inject, Input, OnInit, Output, TemplateRef, ViewChild, ViewEncapsulation } from "@angular/core";
import { ColorPickerPanel } from "./picker-panel";
import { InputGroupModule } from "ts-ui/inputgroup";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { Color, ColorValue, HsbaColorType } from "./color";
import { ChangeFormatEvent, ColorFormatType, CopyColorEvent, OVERLAY_OPTION_DEFAULT, OverlayOptions } from "./interface";
import { OverlayPanel, OverlayPanelModule } from "primeng/overlaypanel";
import { ColorBlock } from "./color-block";
import { Utils } from "./utils";
import { InputTextModule } from "primeng/inputtext";
import { CommonModule } from "@angular/common";
import { Objects } from "ts-ui/helper";
import { DomHandler } from "primeng/dom";
import { TooltipModule } from "primeng/tooltip";
import { ClipboardModule, Clipboard } from "@angular/cdk/clipboard";
const { defaultColor, generateColor } = Utils;
const { isFalse, mergeDeep, notNull, isNull } =Objects;

const INPUT_COLOR_VALUE = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputColor),
    multi: true
};

@Component({
    standalone: true,
    selector: 'ts-color',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, ColorPickerPanel, InputGroupModule,
        OverlayPanelModule, ColorBlock, InputTextModule,
        TooltipModule, ClipboardModule],
    providers: [INPUT_COLOR_VALUE],
    templateUrl: './demo.html'
})
export class InputColor implements OnInit, ControlValueAccessor {

    /**
     * Class of the element.
     * @group Props
     */
    @Input() panelStyleClass: { [key: string]: string };

    /**
     * Class of the element.
     * @group Props
     */
    @Input() panelStyle: { [key: string]: any };

    /**
     * defined value of color 
     * @group Props
     * */
    @Input() value: ColorValue;

    /**
     * defined default value of color
     * @group Props
     * */
    @Input() defaultValue: ColorValue;

    /**
     * defined disabled alpha
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) disabledAlpha: boolean = false;

    /**
     * defined disabled panel
     * @group Props
     * */
  //  @Input({ transform: booleanAttribute }) 
    disabled: boolean = false;

    /**
     * defined setting the title
     * @group Props
     * */
    @Input() title: string | TemplateRef<any>;

    /**
     * defined allow clearing color selected
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) allowClear: boolean = false;

    /**
    * defined show color text
    * @group Props
    * */
    @Input({ transform: booleanAttribute }) inline: boolean = false;

    /**
     * defined format of color
     * @group Props
     * */
    @Input() format: ColorFormatType = 'rgb';

    /**
     * defined close overlay on select color
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) closeOnSelect: boolean = false;

    /**
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) focusOpen: boolean = false;

    /**
     * defined copy icon
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) visibleCopy: boolean = false;

    /**
     * defined copy tooltip
     * @group Props
     * */
    @Input() copyTooltip: string | TemplateRef<HTMLElement>;

    /**
     * defined copy icon
     * @group Props
     * */
    @Input() set copyIcon(icon: string) {
        this._copyIcon = icon ?? 'pi pi-copy';
    }

    get copyIcon(): string {
        return this._copyIcon;
    }

    /**
     * Whether to use overlay API feature. The properties of overlay API can be used like an object in it.
     * @group Props
     */
    @Input() set overlayOptions(opt: OverlayOptions) {
        this._overlayOptions = mergeDeep({ ...OVERLAY_OPTION_DEFAULT }, opt);
    }

    get overlayOptions() {
        return this._overlayOptions;
    }

    /**
     * defined input placeholder
     * @group Props
     * */
    @Input() placeholder: string;

    /**
     * defined input id
     * @group Props
     * */
    @Input() inputId: string;

    /**
     * defined input style
     * @group Props
     * */
    @Input() inputStyle: { [key: string]: any };

    /**
     * defined input readonly
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) readonly: boolean = true;

    /**
     * defined input required
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) required: boolean = false;

    /**
     * emit clear color
     * @group Events
     * */
    @Output() onClear = new EventEmitter<boolean>();

    /**
     * emit change color
     * @group Events
     * */
    @Output() onChange = new EventEmitter<Color>();

    /**
     * emit change color type ('hue' | 'alpha')
     * @group Events
     * */
    @Output() onChangeType = new EventEmitter<HsbaColorType>();

    /**
     * emit change format type ('rgb' | 'hex' | 'hsb')
     * @group Events
     * */
    @Output() onChangeFormat = new EventEmitter<ChangeFormatEvent>();

    /**
     * emit copy color
     * @group Events
     * */
    @Output() onCopy = new EventEmitter<CopyColorEvent>();

    @ViewChild('overlay', { static: false })
    private overlay: OverlayPanel;

    @ViewChild('containerRef', { static: false })
    private containerRef: ElementRef<HTMLDivElement>;

    @ViewChild('input', { static: false })
    private input: ElementRef<HTMLInputElement>;

    formOnChange: (color: Color) => void;
    formOnTouched: () => void;
    color: Color;
    rgbColor: string;


    get toRgbString(): string {
        return this.color?.toRgbString();
    }
    

    private _clipboard = inject(Clipboard);
    private _copyIcon: string = 'pi pi-copy';
    private _overlayOptions = { ...OVERLAY_OPTION_DEFAULT };

    constructor() { }

    generateColor(color: Color) {
        this.color = generateColor(this.value ?? defaultColor);
    }

    ngOnInit(): void {
        this.color = generateColor(this.value ?? defaultColor);
        //this.changeColor(this.color);
    }


    writeValue(color: Color): void {
        //console.log(`writeValue`, color);
        this.color = generateColor(color ?? defaultColor);
    }

    registerOnChange(fn: any): void {
        this.formOnChange = fn;
    }

    registerOnTouched(fn: any): void {
        this.formOnTouched = fn;
    }

    setDisabledState(disabled: boolean): void {
        this.disabled = disabled;
    }

    formattedValue(): string {
        const format = this.format ?? 'rgb';
        return this.color?.toString(format);
    }

    copyColor(): void {
        const colorStr = this.formattedValue();
        this._clipboard.copy(colorStr);
        this.input.nativeElement.select();
        this.onCopy.emit({color: colorStr, origin: this.color, format: this.format});
    }

    changeColor(color: Color, format?: ColorFormatType): void {
        console.log(`changeColor`, color);

        this.color = color;
        this.format = format ?? this.format;

        if (this.formOnChange) {
            this.formOnChange(color);
        }

        this.onChange.emit(color);

        if (notNull(format)) {
            this.onChangeFormat.emit({ origin: color, color: this.formattedValue(), format });
        }
    }

    changeColorType(type: HsbaColorType) {
        if('select' === type) {
            if(!!this.closeOnSelect)this.overlay.hide();
        }
        else this.onChangeType.emit(type);
    }

    clearColor(clear: boolean): void {
        this.onClear.emit(clear);
    }

    showOverlay(event: any): void {
        if (isFalse(this.disabled)) {
            DomHandler.alignOverlay(this.overlay.container,
                this.containerRef.nativeElement, 'self', false);
            this.overlayOptions?.onShow(event)
        }

    }

    toggleOverlay(event: any, ev?: string): void {
        if (isFalse(this.disabled)) {
            const hasShow = isNull(ev) ? true : 'focus' === ev && this.focusOpen;
            if (hasShow) this.overlay.toggle(event);
        }
    }

    styleClsOverlay(): string {
        const array = [`ts-colorpicker-overlay`];
        if (!!this.overlayOptions?.styleClass) {
            array.push(this.overlayOptions?.styleClass);
        }
        return array.join(' ');
    }

    containerCls(): any {
        return {
            [`ts-colorpicker`]: true,
            [`ts-colorpicker-inline`]: !!this.inline
        }
    }

    inputCls(): any {
        return {
            ['ts-color-input']: true
        }
    }

}