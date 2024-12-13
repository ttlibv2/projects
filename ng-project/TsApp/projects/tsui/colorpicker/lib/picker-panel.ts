import { CommonModule } from "@angular/common";
import { AfterContentInit, booleanAttribute, ChangeDetectorRef, Component, ContentChildren, EventEmitter, Input, OnChanges, OnInit, Output, QueryList, SimpleChanges, TemplateRef, ViewEncapsulation } from "@angular/core";
import { Color, ColorValue, HsbaColorType } from "./color";
import { PrimeTemplate } from "primeng/api";
import { Objects } from "ts-ui/helper";
import { Utils } from "./utils";
import { SelectView } from "./panel/select.view";
import { SliderView } from "./panel/slider.view";
import { ColorFormatView } from "./panel/format.view";
import { BlockView } from "./panel/block.view";
import { ChangeFormatEvent, ColorFormatType } from "./interface";
const { notNull , isNull} = Objects;
const { defaultColor } = Utils;

@Component({
    standalone: true,
    selector: 'ts-colorpicker-panel',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, SelectView, SliderView, BlockView, ColorFormatView],
    //templateUrl: './picker-panel.html'
    template: `
        <div class="ts-colorpicker-panel" [ngClass]="panelCls()" [ngStyle]="style">

            @if (panelRenderHeader) {
            <ng-template [ngTemplateOutlet]="panelRenderHeader"></ng-template>
            }

            @if(hasTitle || allowClear) {
            <div class="ts-colorpicker-panel-header" [class.no-title]="!hasTitle">

                @if(hasTitle){
                <div class="ts-colorpicker-panel-header-title">
                    @if(panelRenderTitle) { <ng-container *ngTemplateOutlet="panelRenderTitle"></ng-container>}
                    @else if(titleHasTemplate) { <ng-container *ngTemplateOutlet="$any(title)"></ng-container>}
                    @else if(titleNotBlank) { <span [innerHTML]="title"></span>}
                </div>
                }

                @if (allowClear) {
                <div class="ts-colorpicker-panel-header-clear" (click)="clickClearColor()"></div>
                }

            </div>
            }

            <color-select-view [color]="colorValue" [disabled]="disabled" (onChangeColor)="colorChange($event, 'select')" />

            <!-- slider -->
            <div class="ts-colorpicker-slider-container">

                <div class="ts-colorpicker-slider-group">
                    <color-slider-view type="hue" [color]="colorValue" [disabled]="disabled"
                        [value]="'hsl(' + colorValue?.toHsb()?.h + ',100%, 50%)'" [gradientColors]="hueColor"
                        (onChangeColor)="colorChange($event, 'hue')" />

                    @if(!disabledAlpha) {
                    <color-slider-view type="alpha" [color]="colorValue" [value]="toRgbString" [gradientColors]="gradientColors"
                        [disabled]="disabled" (onChangeColor)="colorChange($event, 'alpha')"/>
                    }
                </div>

                <color-block-view [color]="colorValue"></color-block-view>
            </div>

            <color-format-view [clearColor]="clearColor" [color]="colorValue" [disabledAlpha]="disabledAlpha" [format]="format"
                (onChangeFormat)="formatChange($event)" (onChangeColor)="colorChange($event)" />

            @if (panelRenderFooter) {
            <ng-template [ngTemplateOutlet]="panelRenderFooter"></ng-template>
            }

        </div>
    `
})
export class ColorPickerPanel implements OnInit, AfterContentInit, OnChanges {


    /**
     * Class of the element.
     * @group Props
     */
    @Input() styleClass: { [key: string]: string };

    /**
     * Class of the element.
     * @group Props
     */
    @Input() style: { [key: string]: any };

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
    @Input({ transform: booleanAttribute }) disabled: boolean = false;

    /**
     * defined setting the title
     * @group Props
     * */
    @Input() title: string | TemplateRef<any>;

    /**
     * defined allow clearing color selected
     * @group Props
     * */
    @Input() allowClear: boolean = false;

    /**
     * defined format of color
     * @group Props
     * */
    @Input() format: ColorFormatType = 'hex';

    /**
     * emit clear color
     * @group Events
     * */
    @Output() onClearColor = new EventEmitter<boolean>();

    /**
     * emit clear color
     * @group Events
     * */
    @Output() onChangeColor = new EventEmitter<Color>();

    /**
     * emit change color type
     * @group Events
     * */
    @Output() onChangeColorType = new EventEmitter<HsbaColorType>();

    /**
     * emit change format type
     * @group Events
     * */
    @Output() onChangeFormat = new EventEmitter<ChangeFormatEvent>();


    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;

    panelRenderTitle: TemplateRef<any>;
    panelRenderHeader: TemplateRef<any>;
    panelRenderFooter: TemplateRef<any>;

    hasTitle: boolean;
    clearColor: boolean;
    alphaColor: string = '';

    hueColor: string[] = [
        'rgb(255, 0, 0) 0%',
        'rgb(255, 255, 0) 17%',
        'rgb(0, 255, 0) 33%',
        'rgb(0, 255, 255) 50%',
        'rgb(0, 0, 255) 67%',
        'rgb(255, 0, 255) 83%',
        'rgb(255, 0, 0) 100%'
    ];


    gradientColors: string[] = ['rgba(255, 0, 4, 0) 0%', this.alphaColor];

    colorValue: Color;

    get toRgbString(): string {
        return this.colorValue?.toRgbString();
    }

    constructor(private cdr: ChangeDetectorRef) { }

    ngOnInit(): void {
        this.setColorValue();
    }

    ngAfterContentInit(): void {
        this.templates.forEach(item => {
            switch (item.getType()) {
                case 'panelTitle': this.panelRenderTitle = item.template; break;
                case 'panelHeader': this.panelRenderHeader = item.template; break;
                case 'panelFooter': this.panelRenderFooter = item.template; break;
            }
        });

        this.hasTitle = this._hasTitle();
    }

    ngOnChanges(changes: SimpleChanges): void {
    }


    setColorValue(): void {
        let clr = this.getBlockColor();
        this.updateColorValue(clr);
    }

    hasValue(value: ColorValue): boolean {
        return notNull(value);
    }

    clickClearColor(): void {
        this.clearColor = true;
        this.onClearColor.emit(true);
        this.cdr.markForCheck();
    }

    formatChange(format: ChangeFormatEvent) {
        this.updateColorValue(format.origin);
        this.onChangeFormat.emit(format);
    }

    colorChange(value: Color, type?: HsbaColorType): void {
        const beforeColor = this.colorValue;

        this.updateColorValue(value);

        if (!Objects.equals(value, beforeColor)) {
            this.onChangeColor.emit(value)
        }

        if (type ) {
            this.onChangeColorType.emit(type);
        }
    }

    panelCls(): any {
        return {
            [`ts-colorpicker-panel-disabled-alpha`]: this.disabledAlpha,
            ...this.styleClass
        }
    }

    get titleHasTemplate(): boolean {
        return this.title instanceof TemplateRef;
    }

    get titleNotBlank(): boolean {
        return Objects.notBlank(this.title);
    }

    private updateColorValue(value: Color): void {
        this.clearColor = false;
        this.colorValue = Utils.generateColor(value);
        this.alphaColor = value.toRgbString();
        this.gradientColors = ['rgba(255, 0, 4, 0) 0%', this.alphaColor];
        // this.cdr.markForCheck();
    }

    private getBlockColor(): Color {
        const colorVal = !!this.value ? this.value : !!this.defaultValue ? this.defaultValue : defaultColor;
        return Utils.generateColor(colorVal);
    }

    private _hasTitle(): boolean {
        if (notNull(this.panelRenderTitle)) return true;
        else return this.title && (this.titleHasTemplate || this.titleNotBlank);
    }


}