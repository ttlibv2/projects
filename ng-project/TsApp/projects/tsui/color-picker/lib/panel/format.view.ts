import { booleanAttribute, Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from "@angular/core";
import { FormControl, ValidatorFn, Validators } from "@angular/forms";
import { DropdownModule } from "primeng/dropdown";
import { InputNumberModule } from "primeng/inputnumber";
import { FormGroup, FormsBuilder, FormsModule } from "ts-ui/forms";
import { ColorPickerChangeFormatEvent, ColorFormatType } from "../interface";
import { InputGroupModule } from "ts-ui/input-group";
import { debounceTime, distinctUntilChanged, filter, Subject, takeUntil } from "rxjs";
import { Utils } from '../utils'
import { Color, ColorValue } from "../color";
import { Objects } from "ts-ui/helper";
import { CommonModule } from "@angular/common";
const { notBlank } = Objects;

export interface ColorFormatValue {
    format?: ColorFormatType;
    hex: string;
    hsbH?: number;
    hsbS?: number;
    hsbB?: number;
    rgbR?: number;
    rgbG?: number;
    rgbB?: number;
    roundA?: number;
}

@Component({
    standalone: true,
    selector: 'color-format-view',
    imports: [CommonModule, FormsModule, DropdownModule, InputNumberModule, InputGroupModule],
    template: `
    <form [formGroup]="validateForm" class="ts-colorpicker-color-format sm">
        <div class="ts-colorpicker-color-format-select">
            <p-dropdown [options]="items" optionValue="value" optionLabel="label" formControlName="format" />
        </div>
        <div class="ts-colorpicker-color-format-input" [ngClass]="formatInputCls">
            @switch (isformat) {
                @case ('hex') {
                        <ts-inputgroup prefix="pi pi-hashtag">
                            <input pInputText formControlName="hex" />
                        </ts-inputgroup>
                }

                @case ('hsb') {
                    <p-inputNumber [min]="0" [max]="360" [step]="1" [showButtons]="true" formControlName="hsbH" />
                    <p-inputNumber [min]="0" [max]="100" [step]="1" [showButtons]="true" suffix="%"formControlName="hsbS" />
                    <p-inputNumber [min]="0" [max]="100" [step]="1" [showButtons]="true" suffix="%"formControlName="hsbB" />
                }

                @default {
                    <p-inputNumber [min]="0" [max]="255" [step]="1" [showButtons]="true" formControlName="rgbR" />
                    <p-inputNumber [min]="0" [max]="255" [step]="1" [showButtons]="true" formControlName="rgbG" />
                    <p-inputNumber [min]="0" [max]="255" [step]="1" [showButtons]="true" formControlName="rgbB" />
                } 
            }
        </div>

        @if(!disabledAlpha) {
            <div class="ts-colorpicker-color-format-alpha ts-colorpicker-color-alpha-input">
                <p-inputNumber formControlName="roundA" [min]="0" [max]="100" [step]="1" suffix="%" />
            </div>
        }

    </form>
    `
})
export class FormatView implements OnChanges, OnInit, OnDestroy {
    @Input() format: ColorFormatType;
    @Input() colorValue: ColorValue = '';
    @Input({ transform: booleanAttribute }) clearColor: boolean = false;
    @Input({ transform: booleanAttribute }) disabledAlpha: boolean = false;
    @Output() formatChange = new EventEmitter<ColorPickerChangeFormatEvent>();

    get formatInputCls(): any {
        return {
            [`ts-colorpicker-color-format-input-rgb`]: !notBlank(this.isformat),
            [`ts-colorpicker-color-format-input-${this.isformat}`]: notBlank(this.isformat)
        }
    }

    private destroy$ = new Subject<void>();

    items: any[] = [
        { label: 'HEX', value: 'hex' },
        { label: 'HSB', value: 'hsb' },
        { label: 'RGB', value: 'rgb' },
    ];

    get isformat(): ColorFormatType {
        return this.validateForm.get_value('format');
    }


    validateForm: FormGroup<{
        format: FormControl<string>;
        hex: FormControl<string>;
        hsbH: FormControl<number>;
        hsbS: FormControl<number>;
        hsbB: FormControl<number>;
        rgbR: FormControl<number>;
        rgbG: FormControl<number>;
        rgbB: FormControl<number>;
        roundA: FormControl<number>;
    }>;

    constructor(private fb: FormsBuilder) {
        this.validateForm = fb.group({
            format: ['rgb', Validators.required],
            hex: ['1677FF', this.validatorFn()],
            hsbH: [215], hsbS: [91], hsbB: [100],
            rgbR: [22], rgbG: [119], rgbB: [255],
            roundA: [100]
        });
    }

    validatorFn(): ValidatorFn {
        return control => {
            const REGEXP = /^[0-9a-fA-F]{6}$/;
            if (!control.value) return { error: true };
            else if (!REGEXP.test(control.value)) return { error: true };
            else return null;
        };
    }

    ngOnInit(): void {
        this.validateForm.valueChanges.pipe(
            filter(() => this.validateForm.valid),
            debounceTime(200),
            distinctUntilChanged((prev: any, current) =>
                Object.keys(prev).every(key => prev[key] === current[key])
            ),
            takeUntil(this.destroy$))
            .subscribe(value => {
                let color = '';
                switch (value.format) {
                    case 'hsb':
                        color = this.newHSBA(value).toHsbString();
                        break;
                    case 'rgb':
                        color = this.newRGBA(value).toRgbString();
                        break;
                    default:
                        const hex = Utils.generateColor(value.hex);
                        const hexColor = Utils.generateColor({
                            r: hex.r, g: hex.g,
                            b: hex.b, a: Number(value.roundA) / 100
                        });
                        color = hexColor.getAlpha() < 1 ? hexColor.toHex8String() : hexColor.toHexString();
                        break;
                }
                this.formatChange.emit({ color, format: <any>value.format || this.format || 'hex' });
            });
    }
    newRGBA(value: ColorFormatValue) {
        return Utils.generateColor({
            r: Number(value.rgbR),
            g: Number(value.rgbG),
            b: Number(value.rgbB),
            a: Number(value.roundA) / 100
        });
    }

    private newHSBA(value: ColorFormatValue): Color {
        return Utils.generateColor({
            h: Number(value.hsbH),
            s: Number(value.hsbS) / 100,
            b: Number(value.hsbB) / 100,
            a: Number(value.roundA) / 100
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        const { colorValue, format, clearColor } = changes;
        if (colorValue) {
            this.validateForm.patchValue({
                hex: Utils.generateColor(this.colorValue).toHex(),
                hsbH: Math.round(Utils.generateColor(this.colorValue).toHsb().h),
                hsbS: Math.round(Utils.generateColor(this.colorValue).toHsb().s * 100),
                hsbB: Math.round(Utils.generateColor(this.colorValue).toHsb().b * 100),
                rgbR: Math.round(Utils.generateColor(this.colorValue).r),
                rgbG: Math.round(Utils.generateColor(this.colorValue).g),
                rgbB: Math.round(Utils.generateColor(this.colorValue).b),
                roundA: Math.round(Utils.generateColor(this.colorValue).roundA * 100)
            });
        }

        if (format && this.format) {
            this.validateForm.path_value('format', this.format);
        }

        if (clearColor && this.clearColor) {
            this.validateForm.path_value('roundA', 0);
        }
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

}
