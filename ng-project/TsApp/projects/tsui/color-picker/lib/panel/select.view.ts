
import { DOCUMENT } from '@angular/common';
import {AfterViewInit,  ChangeDetectorRef,  Component,  ElementRef,  EventEmitter,Inject,
     Input,  OnChanges,  OnInit,  Output,   SimpleChanges, ViewChild, ViewEncapsulation, booleanAttribute} from '@angular/core';
import { ColorHandler } from './handler.view';
import { Color, HsbaColorType } from '../color';
import { TransformOffset } from '../interface';
import { Utils } from '../utils';
import { Objects } from 'ts-ui/helper';
import { ColorPalette } from './palette.view';
const {allNotNull, notNull} = Objects;


type EventType = MouseEvent | TouchEvent;
type EventHandle = (e: EventType) => void;
interface Position { pageX: number; pageY: number }

function getPosition(e: EventType): Position{
    const obj = 'touches' in e ? e.touches[0] : e;
    const scrollXOffset = document.documentElement.scrollLeft || document.body.scrollLeft || window.scrollX;
    const scrollYOffset = document.documentElement.scrollTop || document.body.scrollTop || window.scrollY;
    return { pageX: obj.pageX - scrollXOffset, pageY: obj.pageY - scrollYOffset };
}

@Component({
    standalone: true,
    selector: 'color-select-view',
    imports: [ColorHandler, ColorPalette],
    encapsulation: ViewEncapsulation.None,
    template: `
    <div #slider class="ts-colorpicker-color-select"
      (mousedown)="dragStartHandle($event)"
      (touchstart)="dragStartHandle($event)">
      <color-palette-view> 
        <div #transform
          style="position: absolute; z-index: 1;"
          [style.left]="offsetValue.x + 'px'"
          [style.top]="offsetValue.y + 'px'">
          <color-handler-view [color]="toRgbString()" />
        </div>
        <div
          class="ts-colorpicker-saturation" [style.background-color]="toHsb()"
          style=" background-image: linear-gradient(0deg, #000, transparent),  linear-gradient(90deg, #fff, hsla(0, 0%, 100%, 0));" ></div>
      </color-palette-view>
    </div>
  `
})
export class SelectView implements OnInit, AfterViewInit, OnChanges {
    @ViewChild('slider', { static: false }) containerRef!: ElementRef<HTMLDivElement>;
    @ViewChild('transform', { static: false }) transformRef!: ElementRef<HTMLDivElement>;

    @Input() color: Color | null = null;
    @Output() onChangeColor = new EventEmitter<Color>();
    @Output() onChangeColorType = new EventEmitter<HsbaColorType>();
    @Input({ transform: booleanAttribute }) disabled: boolean = false;

    offsetValue: TransformOffset = { x: 0, y: 0 };
    dragRef: boolean = false;


    mouseMoveRef: (e: MouseEvent | TouchEvent) => void = () => null;
    mouseUpRef: (e: MouseEvent | TouchEvent) => void = () => null;

    toRgbString(): string {
        return this.color?.toRgbString() as string;
    }

    toHsb(): string {
        return `hsl(${this.color?.toHsb().h},100%, 50%)`;
    }

    constructor(private cdr: ChangeDetectorRef,
        @Inject(DOCUMENT) private document: Document) { }

    ngOnInit(): void {
        this.document.removeEventListener('mousemove', this.mouseMoveRef);
        this.document.removeEventListener('mouseup', this.mouseUpRef);
        this.document.removeEventListener('touchmove', this.mouseMoveRef);
        this.document.removeEventListener('touchend', this.mouseUpRef);
        this.mouseMoveRef = () => null;
        this.mouseUpRef = () => null;
    }

    ngOnChanges(changes: SimpleChanges): void {
        const { color } = changes;

        if (notNull(color)) {
            if (!this.dragRef && this.containerRef && this.transformRef) {
                const calcOffset = Utils.calculateOffset(this.containerRef,this.transformRef,this.color);
                if (calcOffset) {
                    this.offsetValue = calcOffset;
                    this.cdr.detectChanges();
                }
            }
        }
    }

    ngAfterViewInit(): void {
        if (!this.dragRef && this.containerRef && this.transformRef) {
            const calcOffset = Utils.calculateOffset(this.containerRef, this.transformRef, this.color);
            if (calcOffset) {
                this.offsetValue = calcOffset;
                this.cdr.detectChanges();
            }
        }
    }

    dragStartHandle(e: MouseEvent | TouchEvent): void {
        this.onDragStart(e);
    }

    updateOffset: EventHandle = (e: EventType, direction: 'x' | 'y' = 'y') => {
        const rectContainer = this.containerRef?.nativeElement?.getBoundingClientRect();
        const transformRect = this.transformRef?.nativeElement?.getBoundingClientRect();
        const { x: rectX, y: rectY, width, height } = rectContainer || { x: 0, y: 0, width: 0, height: 0 };
        const { width: targetWidth, height: targetHeight } = transformRect || {width: 0,height: 0 };
        const { pageX, pageY } = getPosition(e);

        const centerOffsetX = targetWidth / 2;
        const centerOffsetY = targetHeight / 2;

        const offsetX = Math.max(0, Math.min(pageX - rectX, width)) - centerOffsetX;
        const offsetY = Math.max(0, Math.min(pageY - rectY, height)) - centerOffsetY;

        const calcOffset = {
            x: offsetX,
            y: direction === 'x' ? this.offsetValue.y : offsetY
        };

        // Exclusion of boundary cases
        if ((targetWidth === 0 && targetHeight === 0) || targetWidth !== targetHeight) {
            return;
        }

        this.offsetValue = calcOffset;
        this.onChangeColor.emit(Utils.calculateColor(calcOffset, this.containerRef, this.transformRef, this.color));
        this.cdr.detectChanges();
    };

    onDragMove: EventHandle = (e: EventType) => {
        e.preventDefault();
        this.updateOffset(e);
    };

    onDragStop: EventHandle = (e: EventType) => {
        e.preventDefault();
        this.dragRef = false;
        this.document.removeEventListener('mousemove', this.onDragMove);
        this.document.removeEventListener('mouseup', this.mouseUpRef);
        this.document.removeEventListener('touchmove', this.mouseMoveRef);
        this.document.removeEventListener('touchend', this.mouseUpRef);
        this.mouseMoveRef = () => null;
        this.mouseUpRef = () => null;
        this.onChangeColorType?.emit();
    };

    onDragStart: EventHandle = (e: EventType) => {
        if (!this.disabled) {
            this.updateOffset(e);
            this.dragRef = true;
            this.documentAddEvent('mousemove', this.onDragMove);
            this.documentAddEvent('mouseup', this.onDragStop);
            this.documentAddEvent('touchmove', this.onDragMove);
            this.documentAddEvent('touchend', this.onDragStop);
            this.mouseMoveRef = this.onDragMove;
            this.mouseUpRef = this.onDragStop;
            this.cdr.markForCheck();
        }
    };

    private documentAddEvent(type: string, listener: any) {
        this.document.addEventListener(type, listener);
    }




}