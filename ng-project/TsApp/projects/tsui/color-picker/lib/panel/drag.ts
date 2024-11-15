import { DOCUMENT } from "@angular/common";
import { AfterViewInit, booleanAttribute, ChangeDetectorRef, Component, ElementRef, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from "@angular/core";
import { TransformOffset } from "../interface";
import { Events } from "ts-ui/core";
import { Utils } from "../utils";
import { Color, HsbaColorType } from "../color";
import { Objects } from "ts-ui/helper";

export type DivElRef = ElementRef<HTMLDivElement>;
export type EventType = MouseEvent | TouchEvent;
export type EventHandle = (e: EventType, ...args: any[]) => void;
export interface Position { posX: number; posY: number }
export interface Offset { x: number; y: number }

export function getPosition(e: EventType): Position {
    const obj = 'touches' in e ? e.touches[0] : e;
    const scrollXOffset = document.documentElement.scrollLeft || document.body.scrollLeft || window.screenX;
    const scrollYOffset = document.documentElement.scrollTop || document.body.scrollTop || window.screenY;
    return { posX: obj.pageX - scrollXOffset, posY: obj.pageY - scrollYOffset };
}

@Component({ standalone: true, template: `` })
export abstract class DragView implements OnInit, OnChanges, AfterViewInit {
    protected readonly cdr = inject(ChangeDetectorRef);
    protected readonly document = inject(DOCUMENT);

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
     * defined value of color 
     * @group Props
     * */
    @Input() color: Color | null = null;

    /**
     * defined disabled panel
     * @group Props
     * */
    @Input({ transform: booleanAttribute })
    disabled: boolean = false;

    @ViewChild('slider', { static: false })
    containerRef!: DivElRef;

    @ViewChild('transform', { static: false })
    transformRef!: DivElRef;

    mouseMoveRef: EventHandle = () => null;
    mouseUpRef: EventHandle = () => null;
    offsetValue: TransformOffset = { x: 0, y: 0 };
    dragRef: boolean = false;

    ngOnInit(): void {
        this.destroyMouseEvent();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if ('color' in changes && this.color) {
            this.updateOffsetValue();
        }
    }

    ngAfterViewInit(): void {
        this.updateOffsetValue();
    }

    dragStartHandle(e: EventType): void {
        this.onDragStart(e);
    }

    onDragMove: EventHandle = (e: EventType) => {
        e.preventDefault();
        this.updateOffset(e);
    };

    onDragStop: EventHandle = (e: EventType) => {
        e.preventDefault();

        this.dragRef = false;

        Events.removeDocumentEvent(this.document, {
            mousemove: this.onDragMove,
            touchmove: this.mouseMoveRef,
            //dragover: this.onDragMove,

            mouseup: this.mouseUpRef,
            touchend: this.mouseUpRef,
            //dragend: this.mouseUpRef

        });
        this.mouseMoveRef = () => null;
        this.mouseUpRef = () => null;
        this.onChangeColorType?.emit();
    };

    onDragStart: EventHandle = (e: EventType) => {
        if (!this.disabled) {
            this.updateOffset(e);
            this.dragRef = true;
            this.mouseMoveRef = this.onDragMove;
            this.mouseUpRef = this.onDragStop;

            Events.addDocumentEvent(this.document, {
                mousemove: this.onDragMove,
                touchmove: this.onDragMove,
                //dragover: this.onDragMove,

                // onDragStop
                mouseup: this.onDragStop,
                touchend: this.onDragStop,
                //dragend: this.onDragStop

            });

            this.cdr.markForCheck();
        }
    };

    updateOffset: EventHandle = (e: EventType, direction: 'x' | 'y' = 'y') => {
        const { x: rectX, y: rectY, width, height } = Utils.clientRect(this.containerRef);
        const { width: targetWidth, height: targetHeight } = Utils.clientRect(this.transformRef);
        const { posX, posY } = getPosition(e);

        const funcCalcOffset = (offset: number, center: number, contSize: number) => {
            return offset < center ? Math.max(0, offset) : offset >= contSize ? contSize : offset;
        };

        const centerOffsetX = targetWidth / 2;
        const centerOffsetY = targetHeight / 2;

        let offsetX, offsetY;


        // check height
        if (height >= targetHeight) {
            offsetY = Math.max(0, Math.min(posY - rectY, height)) - centerOffsetY;
            offsetY = funcCalcOffset(offsetY, centerOffsetY, height - targetHeight);
        }
        else {
            offsetY = -(targetHeight - height) / 2;
        }

        // check width
        offsetX = Math.max(0, Math.min(posX - rectX, width)) - centerOffsetX;
        if (width >= targetWidth) {
            offsetX = funcCalcOffset(offsetX, centerOffsetX, width - targetWidth);
        }


        const calcOffset = { x: offsetX,
            y: direction === 'x' ? this.offsetValue.y : offsetY
        };

        // Exclusion of boundary cases
        const hasChange = !((targetWidth === 0 && targetHeight === 0) || targetWidth !== targetHeight);
        if (hasChange) {
            this.offsetValue = calcOffset;
            this.onChangeColor.emit(this.calculateColor(calcOffset));
            this.cdr.detectChanges();
        }

    };

    protected destroyMouseEvent() {
        Events.removeDocumentEvent(this.document, {
            mousemove: this.mouseMoveRef,
            touchmove: this.mouseMoveRef,
            dragover: this.mouseMoveRef,

            mouseup: this.mouseUpRef,
            touchend: this.mouseUpRef,
        });

        this.mouseMoveRef = () => null;
        this.mouseUpRef = () => null;
    }



    protected updateOffsetValue(): void {
        const hasCalc = !this.dragRef && Objects.allNotNull(this.containerRef, this.transformRef);
        if (hasCalc) {
            const calcOffset = this.calculateOffset();
            if (calcOffset) {
                this.offsetValue = calcOffset;
                this.cdr.detectChanges();
            }
        }
    }



    protected calculateOffset(): TransformOffset {
        return Utils.calculateOffset(
            this.containerRef,
            this.transformRef, this.color
        );
    }

    protected calculateColor(calcOffset: Offset): Color {
        return Utils.calculateColor(
            calcOffset, this.containerRef,
            this.transformRef, this.color,
        );
    }
}