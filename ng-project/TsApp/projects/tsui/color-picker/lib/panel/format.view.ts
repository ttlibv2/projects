import { booleanAttribute, Component, EventEmitter, inject, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from "@angular/core";
import { FormControl, Validators } from "@angular/forms";
import { DropdownChangeEvent, DropdownModule } from "primeng/dropdown";
import { InputNumberModule } from "primeng/inputnumber";
import { FormGroup, FormsBuilder, FormsModule } from "ts-ui/forms";
import { ChangeFormatEvent, ColorFormatType } from "../interface";
import { InputGroupModule } from "ts-ui/input-group";
import { Subject } from "rxjs";
import { Utils } from '../utils'
import { Color } from "../color";
import { Objects } from "ts-ui/helper";
import { CommonModule } from "@angular/common";
import { ButtonModule } from "primeng/button";
import { ClipboardModule, Clipboard } from '@angular/cdk/clipboard';

const { notBlank } = Objects;
const { generateColor, validatorColorHexFn } = Utils;

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


function newRGBA(v: ColorFormatValue): Color {
    return generateColor({
        r: Number(v.rgbR),
        g: Number(v.rgbG),
        b: Number(v.rgbB),
        a: Number(v.roundA) / 100
    });
}

function newHSBA(value: ColorFormatValue): Color {
    return generateColor({
        h: Number(value.hsbH),
        s: Number(value.hsbS) / 100,
        b: Number(value.hsbB) / 100,
        a: Number(value.roundA) / 100
    });
}

function newHex(value: ColorFormatValue): Color {
    const hex = generateColor(value.hex);
    return generateColor({
        r: hex.r, g: hex.g,
        b: hex.b, a: Number(value.roundA) / 100
    });
}



@Component({
    standalone: true,
    selector: 'color-format-view',
    imports: [CommonModule, ClipboardModule, FormsModule, DropdownModule, InputNumberModule, InputGroupModule, ButtonModule],
    template: `
    <form [formGroup]="validateForm" class="ts-colorpicker-format sm">
        <div class="ts-colorpicker-format-select">
            <p-dropdown [options]="items" optionValue="value" optionLabel="label"
                formControlName="format" (onChange)="changeFormat($event)" />
        </div>
        <div class="ts-colorpicker-format-input" [ngClass]="formatInputCls">
            @switch (isformat) {
                @case ('hex') {
                        <ts-inputgroup prefix="pi pi-hashtag" compact="false">
                            <input pInputText formControlName="hex" (focus)="focusHex($event)" />
                            <ng-template pTemplate="suffix">
                                <span class="pi pi-copy" (click)="copyColor()"></span>
                            </ng-template>
                        </ts-inputgroup>
                }

                @case ('hsb') {
                    <p-inputNumber [min]="0" [max]="360" [step]="1" [showButtons]="true" formControlName="hsbH"/>
                    <p-inputNumber [min]="0" [max]="100" [step]="1" [showButtons]="true" suffix="%"formControlName="hsbS"/>
                    <p-inputNumber [min]="0" [max]="100" [step]="1" [showButtons]="true" suffix="%"formControlName="hsbB"/>
                }

                @default {
                    <p-inputNumber [min]="0" [max]="255" [step]="1" [showButtons]="true" formControlName="rgbR"/>
                    <p-inputNumber [min]="0" [max]="255" [step]="1" [showButtons]="true" formControlName="rgbG"/>
                    <p-inputNumber [min]="0" [max]="255" [step]="1" [showButtons]="true" formControlName="rgbB"/>
                } 
            }
        </div>

        @if(!disabledAlpha) {
            <div class="ts-colorpicker-format-alpha ts-colorpicker-color-alpha-input">
                <p-inputNumber formControlName="roundA" [min]="0" [max]="100" [step]="1" suffix="%" [showButtons]="true"/>
            </div>
        }

    </form>
    `
})
export class ColorFormatView implements OnChanges, OnInit, OnDestroy {

    /**
     * defined format of color
     * @group Props
     * */
    @Input() format: ColorFormatType = 'rgb';

    /**
     * defined value of color 
     * @group Props
     * */
    @Input() color: Color;

    /**
     * defined clear color
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) clearColor: boolean = false;

    /**
     * defined disabled alpha
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) disabledAlpha: boolean = false;

    /**
     * emit change format type
     * @group Events
     * */
    @Output() onChangeFormat = new EventEmitter<ChangeFormatEvent>();

    /**
    * emit change color
    * @group Events
    * */
    @Output() onChangeColor = new EventEmitter<Color>();

    get formatInputCls(): any {
        return {
            [`ts-colorpicker-format-input-rgb`]: !notBlank(this.isformat),
            [`ts-colorpicker-format-input-${this.isformat}`]: notBlank(this.isformat)
        }
    }

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

    items: any[] = [
        { label: 'HEX', value: 'hex' },
        { label: 'HSB', value: 'hsb' },
        { label: 'RGB', value: 'rgb' },
    ];

    private destroy$ = new Subject<void>();
    private clipboard = inject(Clipboard);

    constructor(private fb: FormsBuilder) {
        this.validateForm = this.fb.group({
            format: ['rgb', Validators.required],
            hex: ['1677FF', validatorColorHexFn()],
            hsbH: [215], hsbS: [91], hsbB: [100],
            rgbR: [22], rgbG: [119], rgbB: [255],
            roundA: [100]
        });
    }


    ngOnInit(): void {
        this.validateForm.distinctChange(this.destroy$, {
            filter: fg => fg.valid,
            debounceTime: 200,
            comparator: (v1, v2) => v1 === v2,
            subscribe: (value: any) => {
                const beforeColor = this.color;
                switch (value.format) {
                    case 'hsb': this.color = newHSBA(value); break;
                    case 'rgb': this.color = newRGBA(value); break;
                    default: this.color = newHex(value); break;
                }

                if (!this.color.equals(beforeColor)) {
                    this.emitColor(this.color);
                }
            }
        });
    }

    emitColor(color: Color): void {

        this.onChangeColor.emit(color);
    }

    changeFormat(evt: DropdownChangeEvent): void {
        const format: ColorFormatType = evt.value;
        this.onChangeFormat.emit({
            color: this.color.toString(format),
            origin: this.color,
            format: format || this.format || 'hex'
        })
    }

    ngOnChanges(changes: SimpleChanges): void {
        const { color, format, clearColor } = changes;
        if (color && this.color) {
            const color = this.color;
            this.validateForm.patchValue({
                hex: color.toHex(),
                hsbH: Math.round(color.toHsb().h),
                hsbS: Math.round(color.toHsb().s * 100),
                hsbB: Math.round(color.toHsb().b * 100),
                rgbR: Math.round(color.r),
                rgbG: Math.round(color.g),
                rgbB: Math.round(color.b),
                roundA: Math.round(color.roundA * 100)
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

    focusHex(evt: any): void {
        evt.target.select();
    }

    copyColor(): void {
        const colorStr = this.color.toString(this.format);
        this.clipboard.copy(colorStr);
    }


}
