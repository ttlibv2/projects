import { animate, animation, AnimationBuilder, AnimationEvent, AnimationFactory, AnimationPlayer, style, transition, trigger, useAnimation } from "@angular/animations";
import { FocusOrigin } from "@angular/cdk/a11y";
import { hasModifierKey } from "@angular/cdk/keycodes";
import { Platform } from "@angular/cdk/platform";
import { CommonModule, DOCUMENT } from "@angular/common";
import { AfterContentChecked, booleanAttribute, ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, EventEmitter, inject, Input, NgZone, numberAttribute, OnChanges, OnDestroy, Output, Renderer2, SimpleChanges, ViewChild, ViewEncapsulation } from "@angular/core";
import { filter, fromEvent, map, Observable, Subject, take, takeUntil } from "rxjs";
import { INgClass, INgStyle } from "ts-ui/common";
import { Objects } from "ts-ui/helper";

const showAnimation = animation([style({ transform: '{{transform}}', opacity: 0 }), animate('{{transition}}')]);
const hideAnimation = animation([animate('{{transition}}'), style({ transform: '{{transform}}', opacity: 0 })]);

const drawerAnimation = trigger('panelState', [
    transition('void => visible', useAnimation(showAnimation)),
    transition('visible => void', useAnimation(hideAnimation))
]);

export type DrawerPos = 'left' | 'right' | 'top' | 'bottom' | 'full';
export type DrawerResult = 'open' | 'close';

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CommonModule],
    animations: [drawerAnimation],
    selector: 'ts-drawer',
    styleUrl: 'drawer.scss',
    templateUrl: 'drawer.html',
})
export class Drawer implements OnChanges, AfterContentChecked, OnDestroy {

    /** Emits whenever the drawer has started animating. */
    readonly animationStarted = new Subject<AnimationEvent>();

    /** Emits whenever the drawer is done animating. */
    readonly animationEnd = new Subject<AnimationEvent>();

    /**
     * @group Props
     */
    @Input() style: INgStyle;

    /**
     * @group Props
     */
    @Input() styleClass: string;

    /**
     * Specifies the visibility of the dialog.
     * @group Props
     */
    @Input({ transform: booleanAttribute })
    set visible(flag: boolean) {
        this.isOpened = flag;
    }

    get visible(): boolean {
        return this.isOpened;
    }

    /**
     * Transition options of the animation.
     * @group Props
     */
    @Input() transitionOptions: string = '2000ms cubic-bezier(0, 0, 0.2, 1)';

    /**
     * Specifies the position of the drawer, valid values are "left", "right", "bottom" and "top".
     * @group Props
     */
    @Input() position: DrawerPos = 'left';

    /**
     * @group Props
     */
    @Input() mode: 'side' | 'modal' = 'modal';

    /**
     * Width of the Drawer dialog, only when placement is 'right' or 'left'
     * @group Props 
     * */
    @Input() width: number | string;

    /**
     * Width of the Drawer dialog, only when placement is 'top' or 'bottom'
     * @group Props 
     * */
    @Input() height: number | string;

    /**
     * The X coordinate offset(px), only when placement is 'right' or 'left'.	
     * @group Props 
     * */
    @Input({ transform: numberAttribute }) offsetX: number;

    /**
     * The Y coordinate offset(px), only when placement is 'top' or 'bottom'.
     * @group Props 
     * */
    @Input({ transform: numberAttribute }) offsetY: number;

    /**
     * The z-index of the Drawer.
     * @group Props 
     *  */
    @Input({ transform: numberAttribute }) zindex: number;

    /**
     * Whether to show mask or not.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) mask: boolean = true;

    /**
     * Clicking on the mask (area outside the Drawer) to close the Drawer or not.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) maskClosable: boolean = true;

    /**
     * Style for Drawer's mask element.
     * @group Props
     */
    @Input() maskStyle: INgStyle;

    /**
     * Style class for Drawer's mask element.
     * @group Props
     */
    @Input() maskClass: INgClass;

    /**
     * Whether support press esc to close
     * @group Props
     */
    @Input({ transform: booleanAttribute }) keyboard: boolean = true;

    /** Event emitted when the drawer open state is changed. */
    @Output() readonly visibleChange = new EventEmitter<boolean>(false);

    /** Event emitted when the drawer has been opened. */
    @Output() readonly opened = this.visibleChange.pipe(filter(o => o), map(() => { }));

    /** Event emitted when the drawer has been closed. */
    @Output() readonly closed = this.visibleChange.pipe(filter(o => !o), map(() => { }));

    /** Event emitted when the drawer has started opening. */
    @Output() readonly openedStart = this.animationStarted.pipe(
        filter(e => e.fromState !== e.toState && e.toState === 'visible'),
        map(e => e),
    );

    /** Event emitted when the drawer has started closing. */
    @Output() readonly closedStart = this.animationStarted.pipe(
        filter(e => e.fromState !== e.toState && e.toState === 'void'),
        map(e => e),
    );

    //++++++++++++++++++++++++++++++++++++++++++++++++
    transformOptions: any = 'translate3d(-100%, 0, 0)'; // position = left
    private platform = inject(Platform);
    private document = inject(DOCUMENT);
    private renderer = inject(Renderer2);
    private elementRef = inject(ElementRef<HTMLElement>);
    private changeDef = inject(ChangeDetectorRef);
    private ngZone = inject(NgZone);

    private destroy$ = new Subject<void>();
    private isOpened: boolean = false;
    private enableAnimations: boolean = true;
    private openedVia: FocusOrigin;



    constructor() {

        this.animationEnd.subscribe(event => {
            const { fromState, toState } = event;
            const hasOpen = fromState === 'void' && toState === 'visible';
            const hasClose = fromState === 'visible' && toState === 'void';
            if (hasOpen || hasClose) {
                this.visibleChange.emit(this.visible);
            }
        });

        this.ngZone.runOutsideAngular(() => {
            const hasCode = (event: KeyboardEvent) => event.code === 'Escape' && !hasModifierKey(event) && !!this.keyboard && !!this.maskClosable && !!this.mask;
            this.renderer.listen(this.elementRef.nativeElement, 'keydown', event => {
                if(hasCode(event)) {
                    this.ngZone.run(() => {
                        this.hide();
                        event.stopPropagation();
                        event.preventDefault();
                    })
                }
            });
        });

    }

    /** Closes the drawer with context that the mask was clicked. */
    maskClick(): Promise<DrawerResult> {
        return this.setOpen(false, true, 'mouse');
    }

    /**
     * Toggle this drawer.
     * @param isOpen Whether the drawer should be open.
     * @param openedVia Whether the drawer was opened by a key press, mouse click or programmatically.
     * Used for focus management after the sidenav is closed.
     */
    toggle(isOpen?: boolean, openedVia?: FocusOrigin): Promise<DrawerResult> {

        // If the focus is currently inside the drawer content and we are closing the drawer,
        // restore the focus to the initially focused element (when the drawer opened).
        isOpen = Objects.isNull(isOpen) ? !this.isOpened : isOpen;
        if (isOpen && openedVia) this.openedVia = openedVia;

        //Origin to use when restoring focus.
        const focusOrigin = this.openedVia || 'program';

        //Whether focus should be restored on close.
        const restoreFocus = !isOpen && this.isFocusWithinDrawer();

        const result = this.setOpen(isOpen, restoreFocus, focusOrigin);
        if (!isOpen) this.openedVia = null;

        return result;
    }

    /**
     * Open the drawer.
     * @param openedVia Whether the drawer was opened by a key press, mouse click or programmatically.
     * Used for focus management after the sidenav is closed.
     */
    show(openedVia?: FocusOrigin): Promise<DrawerResult> {
        return this.toggle(true, openedVia);
    }

    /** Close the drawer. */
    hide(): Promise<DrawerResult> {
        return this.toggle(false);
    }

    /**
     * Returns drawer width 
     * */
    getWidth(): number {
        const el: HTMLElement = this.elementRef.nativeElement;
        return el?.offsetWidth ?? 0;
    }

    ngOnChanges(changes: SimpleChanges): void {
        const { position, visible } = changes;

        if (position) {
            switch (this.position) {
                case 'left': this.transformOptions = 'translate3d(-100%, 0, 0)'; break;
                case 'right': this.transformOptions = 'translate3d(100%, 0, 0)'; break;
                case 'bottom': this.transformOptions = 'translate3d(0, 100%, 0)'; break;
                case 'top': this.transformOptions = 'translate3d(0, -100%, 0)'; break;
                case 'full': this.transformOptions = 'none'; break;
            }
        }

        if (visible) {
            this.toggle(this.visible);
        }


    }

    ngAfterContentChecked(): void {
        this.enableAnimations = this.platform.isBrowser;
    }

    ngOnDestroy(): void {
        this.animationStarted.complete();
        this.animationEnd.complete();
        this.destroy$.next();
        this.destroy$.complete();
    }

    containerCls(): any {
        return {
            'ts-drawer': true,
            ['ts-drawer-active']: this.visible,
            ['ts-drawer-left']: this.position === 'left',
            ['ts-drawer-right']: this.position === 'right',
            ['ts-drawer-top']: this.position === 'top',
            ['ts-drawer-bottom']: this.position === 'bottom',
            ['ts-drawer-full']: this.position === 'full',
            [this.styleClass]: !!this.styleClass
        }
    }

    /** Whether focus is currently within the drawer. */
    private isFocusWithinDrawer(): boolean {
        const activeEl = this.document.activeElement;
        return !!activeEl && this.elementRef.nativeElement.contains(activeEl);
    }

    /**
     * Toggles the opened state of the drawer.
     * @param isOpen Whether the drawer should open or close.
     * @param restoreFocus Whether focus should be restored on close.
     * @param focusOrigin Origin to use when restoring focus.
     */
    private setOpen(isOpen: boolean, restoreFocus: boolean, focusOrigin: Exclude<FocusOrigin, null>): Promise<DrawerResult> {

        this.isOpened = isOpen;

        //Whether focus should be restored on close.
        if (restoreFocus) {
            this.restoreFocus(focusOrigin);
        }

        // Needed to ensure that the closing sequence fires off correctly.
        this.changeDef.markForCheck();
        this.updateFocusTrapState();

        return new Promise<'open' | 'close'>(resolve => {
            this.visibleChange.pipe(take(1)).subscribe(open => resolve(open ? 'open' : 'close'));
        });

    }

    /** Updates the enabled state of the focus trap. */
    private updateFocusTrapState() {
        // if (this.focusTrap) {
        //     // Trap focus only if the backdrop is enabled. 
        //     // Otherwise, allow end user to interact with the sidenav content.
        //     this.focusTrap.enabled = !!this.maskClosable && this.opened;
        // }
    }

    /**
     * Restores focus to the element that was originally focused when the drawer opened.
     * If no element was focused at that time, the focus will be restored to the drawer.
     */
    private restoreFocus(focusOrigin: Exclude<FocusOrigin, null>) {
        // if (this.autoFocus === 'dialog') {
        //     return;
        // }

        // if (this._elementFocusedBeforeDrawerWasOpened) {
        //     this._focusMonitor.focusVia(this._elementFocusedBeforeDrawerWasOpened, focusOrigin);
        // } else {
        //     this._elementRef.nativeElement.blur();
        // }

        // this._elementFocusedBeforeDrawerWasOpened = null;
    }

}