
import { DOCUMENT, NgClass } from '@angular/common';
import {
    AfterViewInit,
    ChangeDetectorRef,
    Component,
    ElementRef,
    EventEmitter,
    Inject,
    Input,
    OnChanges,
    OnInit,
    Output,
    SimpleChanges,
    ViewChild,
    ViewEncapsulation,
    booleanAttribute,
    inject
} from '@angular/core';
import { Color, HsbaColorType } from '../color';
import { TransformOffset } from '../interface';
import { Utils } from '../utils';
import { ColorPalette } from './palette.view';
import { GradientView } from './gradient.view';
import { ColorHandler } from './handler.view';
import { DivElRef, DragView, Offset } from './drag';


// type EventType = MouseEvent | TouchEvent;

// type EventHandle = (e: EventType) => void;

// function getPosition(e: EventType): { pageX: number; pageY: number } {
//     const obj = 'touches' in e ? e.touches[0] : e;
//     const scrollXOffset = document.documentElement.scrollLeft || document.body.scrollLeft || window.screenX;
//     const scrollYOffset = document.documentElement.scrollTop || document.body.scrollTop || window.screenY;
//     return { pageX: obj.pageX - scrollXOffset, pageY: obj.pageY - scrollYOffset };
// }

@Component({
    selector: 'color-slider-view',
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    imports: [ColorPalette, GradientView, ColorHandler, NgClass],
    template: `
    <div #slider class="ts-colorpicker-slider"
      [ngClass]="'ts-colorpicker-slider-' + type"
      (mousedown)="dragStartHandle($event)"
      (touchstart)="dragStartHandle($event)">
      <color-palette-view>
        <div #transform style="position: absolute; z-index: 1;"
          [style.left]="offsetValue.x + 'px'"
          [style.top]="offsetValue.y + 'px'">
          <color-handler-view size="small" [colorRgb]="value"></color-handler-view>
        </div>
        <color-gradient-view [colors]="gradientColors" [direction]="direction" [type]="type"></color-gradient-view>
      </color-palette-view>
    </div>
  `,
    styles: [
        `
      :host {
        display: block;
        width: 100%;
      }
    `
    ]
})
export class SliderView extends DragView implements OnInit, AfterViewInit, OnChanges {
    @Input() gradientColors: string[] = [];
    @Input() direction: string = 'to right';
    @Input() type: HsbaColorType = 'hue';

    @Input() value: string | null = null;

    // /**
    //  * defined disabled panel
    //  * @group Props
    //  * */
    // @Input({ transform: booleanAttribute })
    // override disabled: boolean = false;


    //@Output() override onChangeColor = new EventEmitter<Color>();
    //@Output() override onChangeColorType = new EventEmitter<HsbaColorType>();

   // @ViewChild('slider', { static: false })
    //containerRef!: DivElRef;

    //@ViewChild('transform', { static: false })
    //transformRef!: DivElRef;

    override ngAfterViewInit(): void {
        super.ngAfterViewInit();
    }

    protected override calculateColor(calcOffset: Offset): Color {
        return Utils.calculateColor(
            calcOffset, this.containerRef,
            this.transformRef, this.color, this.type
        );

    }

    protected override calculateOffset(): TransformOffset {
        return Utils.calculateOffset(
            this.containerRef, this.transformRef,
            this.color, this.type
        );
    }

}