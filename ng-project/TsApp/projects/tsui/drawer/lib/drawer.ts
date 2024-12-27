import { CommonModule } from "@angular/common";
import { AfterContentInit, booleanAttribute, ChangeDetectionStrategy, Component, ContentChildren, EventEmitter, Input, numberAttribute, OnChanges, OnDestroy, OnInit, Output, QueryList, SimpleChanges, TemplateRef, ViewEncapsulation } from "@angular/core";
import { AnyTemplateOutlet, ButtonProps, INgClass, INgStyle, pButtonCustom, QueryPTemplate, StringTemplate } from "ts-ui/common";
import { DrawerMode, DrawerPos, HeaderContext } from "./idrawer";
import { animate, animation, AnimationEvent, style, transition, trigger, useAnimation } from "@angular/animations";
import { Direction } from "@angular/cdk/bidi";
import { coerceCssPixelValue } from "@angular/cdk/coercion";
import { PrimeTemplate } from "primeng/api";
import { ButtonDirective } from "primeng/button";

export const drawerMaskMotion = trigger('drawerMaskMotion', [
    transition(':enter', [style({ opacity: 0 }), animate(`0.3s`, style({ opacity: 1 }))]),
    transition(':leave', [style({ opacity: 1 }), animate(`0.3s`, style({ opacity: 0 }))])
]);

const showAnimation = animation([style({ transform: '{{transform}}', opacity: 0 }), animate('{{transition}}')]);
const hideAnimation = animation([animate('{{transition}}', style({ transform: '{{transform}}', opacity: 0 }))]);
const triggerAnimation = trigger('panelState', [transition('hide => visible', [useAnimation(showAnimation)]), transition('visible => hide', [useAnimation(hideAnimation)])]);

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CommonModule, AnyTemplateOutlet, ButtonDirective, pButtonCustom],
    animations: [triggerAnimation, drawerMaskMotion],
    styles: [`ts-drawer { display: block; } `],
    selector: 'ts-drawer',
    templateUrl: './drawer.html',
    host: {
        'class': 'ts-drawer',
        '[class.ts-drawer-rtl]': `dir === 'rtl'`,
        '[class.ts-drawer-open]': `isOpen`,
        '[class.ts-drawer-left]': `position == 'left' && !fullscreen`,
        '[class.ts-drawer-right]': `position == 'right' && !fullscreen`,
        '[class.ts-drawer-top]': `position == 'top' && !fullscreen`,
        '[class.ts-drawer-bottom]': `position == 'bottom' && !fullscreen`,
        '[class.ts-drawer-full]': `position == 'full' || fullscreen`,
        '[class.ts-drawer-side]': `mode == 'side'`,
        '[class.ts-drawer-modal]': `mode == 'modal'`,
        '[style.transform]': 'offsetTransform',
        '[style.zIndex]': 'zindex'

    }
})
export class Drawer implements OnInit, OnChanges, AfterContentInit, OnDestroy {

    /**
     * Whether to block scrolling of the document when drawer is active.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) blockScroll: boolean = false;

    /**
     * Whether to automatically manage layering.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) autoZIndex: boolean = true;

    /**
     * zIndex value to use in layering.
     * @group Props
     */
    @Input({ transform: numberAttribute }) zindex: number;

    /**
     * Specifies if pressing escape key should hide the drawer.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) closeOnEscape: boolean = true;

    /**
     * Transition options of the animation.
     * @group Props
     */
    @Input() transitionOptions: string = '150ms cubic-bezier(0, 0, 0.2, 1)';

    /**
     * Specifies the position of the drawer.
     * @group Props
     */
    @Input() position: DrawerPos = 'left';


    /**
     * Specifies the MODE of the drawer.
     * @group Props
     */
    @Input() mode: DrawerMode = 'modal';

    /**
     * Whether to show mask or not.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) mask: boolean = true;

    /**
     * Style of the mask.
     * @group Props
     */
    @Input() maskStyle: INgStyle;

    /**
     * Style class of the mask.
     * @group Props
     */
    @Input() maskClass: INgClass;

    @Input() contentWrapperClass: INgClass;

    /**
     * Width of the Drawer dialog, only when position is 'right' or 'left'
     * @group Props
     * */
    @Input() set width(w: number | string) {
        this._width = coerceCssPixelValue(w);
    }

    get width(): string {
        return this._width;
    }

    /**
     * Height of the Drawer dialog, only when position is 'top' or 'bottom'
     * @group Props
     * */
    @Input() set height(h: number | string) {
        this._height = coerceCssPixelValue(h);
    }

    get height(): string {
        return this._height;
    }

    /**
     * The X coordinate offset(px), only when position is 'right' or 'left'.
     * @group Props
     */
    @Input({ transform: numberAttribute }) offsetX: number = 0;

    /**
     * The Y coordinate offset(px), only when position is 'top' or 'bottom'.
     * @group Props
     */
    @Input({ transform: numberAttribute }) offsetY: number = 0;

    /**
     * Specifies the visibility of the dialog.
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) visible: boolean;

    @Input({ transform: booleanAttribute }) visibleHeader: boolean = true;

    /**
     * Whether to display close button.
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) closable: boolean = true;

    /**
     * Close icon of the dialog.
     * @group Props
     * */
    @Input() closeIcon: string = 'pi pi-times';

    @Input() closeOption: ButtonProps;

    /**
     * Extra actions area at corner.
     * @group Props
     * */
    @Input() headerExtra: StringTemplate;

    /**
     * The header for Drawer.
     * @group Props
     * */
    @Input() headerTitle: StringTemplate;

    /**
    * The custom header for Drawer.
    * @group Props
    * */
    @Input() header: TemplateRef<any>;

    /**
     * The footer for Drawer.
     * @group Props
     * */
    @Input() footer: TemplateRef<any>;

    /**
     * Body style for drawer body element. Such as height, padding etc.
     * @group Props
     * */
    @Input() bodyStyle: INgStyle;

    /**
     * Body style class for drawer body element
     * @group Props
     */
    @Input() bodyClass: INgClass;

    @Input({ transform: booleanAttribute }) fullscreen: boolean;


    @Output() readonly onMask = new EventEmitter<MouseEvent>();
    @Output() readonly onClose = new EventEmitter<MouseEvent>();
    @Output() readonly onVisible = new EventEmitter<boolean>();

    @ContentChildren(PrimeTemplate)
    private _templates: QueryList<PrimeTemplate>;

    //+++++++++++++++++++++++++++++++++++++++++++++++++++
    isOpen: boolean = false;
    dir: Direction = 'ltr';

    headerContext: HeaderContext;

    private _width: string;
    private _height: string;
    private _pTemplateUtil: QueryPTemplate;
    private _pTemplates: any = {};

    get headerTemplate() { 
        return this.header || this._pTemplates['header'];
    }

    get offsetTransform(): string {

        if (!this.isOpen || this.offsetX + this.offsetY === 0) {
            return null;
        }

        if (this.fullscreen) return 'none';
        else switch (this.position) {
            case 'left': return `translateX(${this.offsetX}px)`;
            case 'right': return `translateX(-${this.offsetX}px)`;
            case 'top': return `translateY(${this.offsetY}px)`;
            case 'bottom': return `translateY(-${this.offsetY}px)`;
            default: return 'none';
        }
    }

    get transformOptions(): string {
        if (this.isOpen) return null;
        else if (this.fullscreen) return 'none';
        else switch (this.position) {
            case 'left': return `translate3d(-100%, 0, 0)`;
            case 'right': return `translate3d(100%, 0, 0)`;
            case 'top': return `translate3d(0, -100%, 0)`;
            case 'bottom': return `translate3d(0, 100%, 0)`;
            case 'full': return 'none';
        }
    }

    onAnimationStart(event: AnimationEvent): void {}

    onAnimationEnd(event: AnimationEvent): void { }

    maskClick(): void {
        this.onMask.emit();
    }

    close(event: any): void {

    }

    ngOnInit(): void {
        this.headerContext = {
            closable: this.closable,
            closeIcon: this.closeIcon,
            headerExtra: this.headerExtra,
            headerTitle: this.headerTitle
        };
    }


    ngOnChanges(changes: SimpleChanges): void {

    }

    ngAfterContentInit(): void {
        this._pTemplateUtil = QueryPTemplate.register({
            templates: this._templates,
            onClear: () => this._pTemplates = undefined,
            onUpdate: list => this._pTemplates = list
        });
    }

    ngOnDestroy(): void {
        this._pTemplateUtil?.destroy();
    }

}
