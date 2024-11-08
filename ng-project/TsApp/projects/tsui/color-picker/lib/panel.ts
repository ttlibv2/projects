import { CommonModule } from "@angular/common";
import { AfterContentInit, booleanAttribute, ChangeDetectorRef, Component, ContentChildren, EventEmitter, Input, OnChanges, OnInit, Output, QueryList, SimpleChanges, TemplateRef, ViewEncapsulation } from "@angular/core";
import { Color, ColorValue, HsbaColorType } from "./color";
import { PrimeTemplate } from "primeng/api";
import { Objects } from "ts-ui/helper";
import { Utils } from "./utils";
import { SelectView } from "./panel/select.view";
import { SliderView } from "./panel/slider.view";
import { FormatView } from "./panel/format.view";
import { BlockView } from "./panel/block.view";
import { ColorFormatType } from "./interface";
const { anyNotNull, notNull, isTemplateRef } = Objects;

@Component({
    standalone: true,
    selector: 'ts-colorpicker-panel',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, SelectView, SliderView, BlockView, FormatView],
    template: `
        <div class="ts-colorpicker-panel" [class.ts-colorpicker-panel-disabled]="disabled">

            @if (panelRenderHeader) {
                <ng-template [ngTemplateOutlet]="panelRenderHeader"></ng-template>
            }

            @if(title || titleTemplate || allowClear) {
                <div class="ts-colorpicker-panel-header-title">
                    <div class="ts-colorpicker-panel-header-title-content">
                        @if(titleTemplate) { <ng-container *ngTemplateOutlet="titleTemplate"></ng-container>}
                        @else if(titleIsTemplateRef()) { <ng-container *ngTemplateOutlet="titleTemplateRef"></ng-container>}
                        @else { <span [innerHTML]="title"></span>}
                    </div>

                    @if (allowClear) {
                        <div class="ts-colorpicker-panel-header-title-clear" (click)="clearColorHandle()"></div>
                    }
                    
                </div>
            }

            <color-select-view
              [color]="colorValue" [disabled]="disabled"
              (onChangeColor)="handleChangeColorType($event)"
              (onChangeColorType)="onChangeComplete.emit($event)"/>

            <!-- slider -->
            <div class="ts-colorpicker-color-slider-container">

                <div class="ts-colorpicker-color-slider-group">
                    <color-slider-view
                      type="hue" [color]="colorValue" [disabled]="disabled"
                      [value]="'hsl(' + colorValue?.toHsb()?.h + ',100%, 50%)'"
                      [gradientColors]="hueColor"
                      (onChange)="handleChangeColorType($event, 'hue')"
                      (onChangeComplete)="onChangeComplete.emit($event)"/>

                    @if(!disabledAlpha) {
                        <color-slider-view
                          type="alpha" [color]="colorValue" [value]="toRgbString"
                          [gradientColors]="gradientColors" [disabled]="disabled"
                          (onChange)="handleChangeColorType($event, 'alpha')"
                          (onChangeComplete)="onChangeComplete.emit($event)"/>
                    }
                </div>

                <color-block-view [color]="toRgbString"></color-block-view>
            </div>

            <color-format-view 
                [clearColor]="clearColor" 
                [colorValue]="colorValue"
                [format]="format"
                [disabledAlpha]="disabledAlpha"
                (formatChange)="formatChange($event)"
             />

            @if (panelRenderFooter) {
                <ng-template [ngTemplateOutlet]="panelRenderFooter"></ng-template>
            }
        </div>
    `
})
export class ColorPickerPanel implements OnInit, AfterContentInit, OnChanges {

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
    @Input() format: ColorFormatType = 'rgb';














    @Output() onChangeColorType = new EventEmitter<{ color: Color; type?: HsbaColorType }>();
    @Output() onChangeComplete = new EventEmitter<HsbaColorType>();
    @Output() onClearColor = new EventEmitter<boolean>();

    get titleTemplateRef(): TemplateRef<any> {
        return this.title instanceof TemplateRef ? this.title : undefined;
    }

    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;

    panelRenderHeader: TemplateRef<any>;
    panelRenderFooter: TemplateRef<any>;
    titleTemplate: TemplateRef<any>;

    colorValue: Color | null = null;
    clearColor: boolean = false;
    alphaColor: string = '';

    gradientColors: string[] = ['rgba(255, 0, 4, 0) 0%', this.alphaColor];
    toRgbString: string = this.colorValue?.toRgbString() || '';

    hueColor: string[] = [
        'rgb(255, 0, 0) 0%',
        'rgb(255, 255, 0) 17%',
        'rgb(0, 255, 0) 33%',
        'rgb(0, 255, 255) 50%',
        'rgb(0, 0, 255) 67%',
        'rgb(255, 0, 255) 83%',
        'rgb(255, 0, 0) 100%'
    ];

    constructor(private cdr: ChangeDetectorRef) { }

    ngOnInit(): void {
        this.setColorValue(this.value);
    }

    ngOnChanges(changes: SimpleChanges): void {
        const { value, defaultValue } = changes;
        if (anyNotNull(value, defaultValue)) {
            this.setColorValue(this.value);
        }
    }

    clearColorHandle(): void {
        this.clearColor = true;
        this.onClearColor.emit(true);
        this.cdr.markForCheck();
    }

    hasValue(value: ColorValue): boolean {
        return notNull(value);
    }

    setColorValue(color: ColorValue): void {
        let mergeState;
        if (this.hasValue(color)) { mergeState = color; }
        else if (this.hasValue(this.defaultValue)) {
            mergeState = this.defaultValue;
        }
        else { mergeState = Utils.defaultColor; }

        this.colorValue = Utils.generateColor(mergeState);
        this.setAlphaColor(this.colorValue);
        this.toRgbString = this.colorValue?.toRgbString() || '';
        this.cdr.detectChanges();
    }

    setAlphaColor(colorValue: Color): void {
        const rgb = Utils.generateColor(colorValue.toRgbString());
        this.alphaColor = rgb.toRgbString();
        this.gradientColors = ['rgba(255, 0, 4, 0) 0%', this.alphaColor];
        this.cdr.markForCheck();
    }

    handleChangeColorType(color: Color, type?: HsbaColorType): void {
        this.setColorValue(color);
        this.onChangeColorType.emit({ color, type });
    }

    ngAfterContentInit(): void {
        this.templates.forEach(item => {
            switch (item.getType()) {
                case 'headerPanel': this.panelRenderHeader = item.template; break;
                case 'footerPanel': this.panelRenderFooter = item.template; break;
                case 'titlePanel': this.titleTemplate = item.template; break;
            }
        })
    }

    titleIsTemplateRef(): boolean {
        return this.title instanceof TemplateRef;
    }

    formatChange(value: { color: string; format: ColorFormatType }): void {
        this.value = value.color;
        this.clearColor = false;
        this.getBlockColor();
        this.onChange.emit({ color: Utils.generateColor(value.color), format: value.format });
        this.formControl.patchValue(value.color);
        this.cdr.markForCheck();
    }

}