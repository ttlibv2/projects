import { AfterContentChecked, afterNextRender, AfterViewInit, booleanAttribute, ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, EventEmitter, inject, Injector, Input, NgZone, numberAttribute, OnChanges, OnDestroy, OnInit, Output, Renderer2, SimpleChanges, ViewChild, ViewEncapsulation, } from '@angular/core';
import { FocusMonitor, FocusOrigin, FocusTrap, FocusTrapFactory, InteractivityChecker, } from '@angular/cdk/a11y';
import { BooleanInput, coerceBooleanProperty } from '@angular/cdk/coercion';
import { ESCAPE, hasModifierKey } from '@angular/cdk/keycodes';
import { Platform } from '@angular/cdk/platform';
import { CdkScrollable } from '@angular/cdk/scrolling';
import { DOCUMENT } from '@angular/common';
import { AnimationEvent } from '@angular/animations';
import { fromEvent, Observable, Subject } from 'rxjs';
import { filter, map, take, takeUntil } from 'rxjs/operators';
import { matDrawerAnimations } from './drawer-animations';
import { DrawerContainer } from './drawer-container';
import { AutoFocusTarget, DRAWER_CONTAINER, DrawerMode, DrawerPosition, DrawerToggleResult } from './drawer.interface';
import { INgStyle } from 'ts-ui/common';



@Component({
    standalone: true,
    animations: [matDrawerAnimations],
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [CdkScrollable],
    selector: 'ts-drawer',
    templateUrl: 'drawer.html',
    host: {
        'class': 'ts-drawer',
        '[class.ts-drawer-left]': `position == 'left'`,
        '[class.ts-drawer-right]': `position == 'right'`,
        '[class.ts-drawer-top]': `position == 'top'`,
        '[class.ts-drawer-bottom]': `position == 'bottom'`,
        '[class.ts-drawer-full]': `position == 'full'`,
        //'[style.transform]': 'transform',





        // '[class.ts-drawer-end]': 'position === "end"',
        '[class.ts-drawer-over]': 'mode === "over"',
        '[class.ts-drawer-push]': 'mode === "push"',
        '[class.ts-drawer-side]': 'mode === "side"',
        '[class.ts-drawer-opened]': 'opened',
        'tabIndex': '-1',
        // '[@transform]': '_animationState',
        // '(@transform.start)': '_animationStarted.next($event)',
        // '(@transform.done)': '_animationEnd.next($event)',
    },
})
export class Drawer implements OnInit, OnChanges, AfterViewInit, AfterContentChecked, OnDestroy {

    /** The side that the drawer is attached to. */
    @Input() position: DrawerPosition = 'left';

    /**
     * Whether to automatically manage layering.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) autoZIndex: boolean = true;

    /**
     * Base zIndex value to use in layering.
     * @group Props
     */
    @Input({ transform: numberAttribute }) baseZIndex: number = 0;

    /**
     * Transition options of the animation.
     * @group Props
     */
    @Input() transitionOptions: string = '150ms cubic-bezier(0, 0, 0.2, 1)';

    /** Mode of the drawer; one of 'over', 'push' or 'side'. */
    @Input() mode: DrawerMode = 'over';

    /** Whether to show mask or not. */
    @Input({ transform: booleanAttribute }) mask: boolean = true;

    /** 	Style for Drawer's mask element. */
    @Input() maskStyle: INgStyle;

    /** 	Class for Drawer's mask element. */
    @Input() maskClass: string;

    /** 	Clicking on the mask (area outside the Drawer) to close the Drawer or not. */
    @Input({ transform: booleanAttribute }) maskClosable: boolean = true;























    /** Whether the drawer can be closed with the escape key or by clicking on the backdrop. */
    @Input()
    get disableClose(): boolean {
        return this._disableClose;
    }
    set disableClose(value: BooleanInput) {
        this._disableClose = coerceBooleanProperty(value);
    }
    private _disableClose: boolean = false;

    /**
     * Whether the drawer is opened. We overload this because we trigger an event when it
     * starts or end.
     */
    @Input({ transform: booleanAttribute })
    set visible(vs: boolean) {
        this.isOpen = vs;
    }

    get visible(): boolean {
        return this.isOpen;
    }



    // get opened(): boolean {
    //     return this._opened;
    // }
    // set opened(value: BooleanInput) {
    //     this.toggle(coerceBooleanProperty(value));
    // }
    // private _opened: boolean = false;

    /** How the sidenav was opened (keypress, mouse click etc.) */
    private _openedVia: FocusOrigin | null;
    isOpen: boolean = false;

    /** Emits whenever the drawer has started animating. */
    readonly _animationStarted = new Subject<AnimationEvent>();

    /** Emits whenever the drawer is done animating. */
    readonly _animationEnd = new Subject<AnimationEvent>();

    /** Current state of the sidenav animation. */
    _animationState: 'open-instant' | 'open' | 'void' = 'void';

    /** Event emitted when the drawer open state is changed. */
    @Output() readonly openedChange = new EventEmitter<boolean>(true);

    /** Event emitted when the drawer has been opened. */
    @Output('opened')
    readonly _openedStream = this.openedChange.pipe(
        filter(o => o),
        map(() => { }),
    );

    /** Event emitted when the drawer has started opening. */
    @Output()
    readonly openedStart: Observable<void> = this._animationStarted.pipe(
        filter(e => e.fromState !== e.toState && e.toState.indexOf('open') === 0),
        map(() => undefined),
    );

    /** Event emitted when the drawer has been closed. */
    @Output('closed')
    readonly _closedStream = this.openedChange.pipe(
        filter(o => !o),
        map(() => { }),
    );

    /** Event emitted when the drawer has started closing. */
    @Output()
    readonly closedStart: Observable<void> = this._animationStarted.pipe(
        filter(e => e.fromState !== e.toState && e.toState === 'void'),
        map(() => undefined),
    );

    /** Emits when the component is destroyed. */
    private readonly _destroyed = new Subject<void>();

    /** Event emitted when the drawer's position changes. */
    // tslint:disable-next-line:no-output-on-prefix
    @Output('positionChanged') readonly onPositionChanged = new EventEmitter<void>();

    /** Reference to the inner element that contains all the content. */
    @ViewChild('content') _content: ElementRef<HTMLElement>;

    /**
     * An observable that emits when the drawer mode changes. This is used by the drawer container to
     * to know when to when the mode changes so it can adapt the margins on the content.
     */
    readonly _modeChanged = new Subject<void>();

    private _injector = inject(Injector);
    private _changeDetectorRef = inject(ChangeDetectorRef);

    readonly elementRef = inject<ElementRef<HTMLElement>>(ElementRef);
    readonly platform = inject(Platform);
    readonly ngZone = inject(NgZone);
    readonly renderer = inject(Renderer2);
    readonly interactivityChecker = inject(InteractivityChecker);
    readonly document = inject(DOCUMENT, { optional: true })!;
    readonly container = inject<DrawerContainer>(DRAWER_CONTAINER, { optional: true });
    readonly focusTrapFactory = inject(FocusTrapFactory);
    readonly focusMonitor = inject(FocusMonitor);

    focusTrap: FocusTrap | null = null;
    elementFocusedBeforeDrawerWasOpened: HTMLElement | null = null;

    /** Whether the drawer is initialized. Used for disabling the initial animation. */
    private _enableAnimations = false;

    /** Whether the view of the component has been attached. */
    private _isAttached: boolean;

    /** Anchor node used to restore the drawer to its initial position. */
    private _anchor: Comment | null;

    get transform(): string {

        if (this.isOpen) {
            return null;
        }

        switch (this.position) {
            case 'full': return 'none';
            case 'left': return 'translate3d(-100%, 0px, 0px)';
            case 'right': return 'translate3d(100%, 0px, 0px)';
            case 'bottom': return 'translate3d(0px, 100%, 0px)';
            case 'top': return 'translate3d(0px, -100%, 0px)';
        }
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++

    transformOptions: any = 'translate3d(-100%, 0px, 0px)';

    constructor(...args: unknown[]);

    constructor() {


        /**
         * Listen to `keydown` events outside the zone so that change detection is not run every
         * time a key is pressed. Instead we re-enter the zone only if the `ESC` key is pressed
         * and we don't have close disabled.
         */
        this.ngZone.runOutsideAngular(() => {
            (fromEvent(this.elementRef.nativeElement, 'keydown') as Observable<KeyboardEvent>)
                .pipe(
                    filter(event => {
                        return event.keyCode === ESCAPE && !this.disableClose && !hasModifierKey(event);
                    }),
                    takeUntil(this._destroyed),
                )
                .subscribe(event =>
                    this.ngZone.run(() => {
                        this.close();
                        event.stopPropagation();
                        event.preventDefault();
                    }),
                );
        });

        this._animationEnd.subscribe((event: AnimationEvent) => {
            const { fromState, toState } = event;

            if (
                (toState.indexOf('open') === 0 && fromState === 'void') ||
                (toState === 'void' && fromState.indexOf('open') === 0)
            ) {
                this.openedChange.emit(this.isOpen);
            }
        });
    }

    ngOnInit(): void {

    }

    ngOnChanges(changes: SimpleChanges): void {
        const { mode, visible } = changes;
        // if(position) this.changePosition();
        if (visible) this.toggle(this.visible);
        if (mode) {
            this._updateFocusTrapState();
            this._modeChanged.next();
        }
    }



    /**
     * Focuses the provided element. If the element is not focusable, it will add a tabIndex
     * attribute to forcefully focus it. The attribute is removed after focus is moved.
     * @param element The element to focus.
     */
    private _forceFocus(element: HTMLElement, options?: FocusOptions) {
        if (!this.interactivityChecker.isFocusable(element)) {
            element.tabIndex = -1;
            // The tabindex attribute should be removed to avoid navigating to that element again
            this.ngZone.runOutsideAngular(() => {
                const callback = () => {
                    cleanupBlur();
                    cleanupMousedown();
                    element.removeAttribute('tabindex');
                };

                const cleanupBlur = this.renderer.listen(element, 'blur', callback);
                const cleanupMousedown = this.renderer.listen(element, 'mousedown', callback);
            });
        }
        element.focus(options);
    }

    /**
     * Focuses the first element that matches the given selector within the focus trap.
     * @param selector The CSS selector for the element to set focus to.
     */
    private _focusByCssSelector(selector: string, options?: FocusOptions) {
        let elementToFocus = this.elementRef.nativeElement.querySelector(
            selector,
        ) as HTMLElement | null;
        if (elementToFocus) {
            this._forceFocus(elementToFocus, options);
        }
    }

    /** Whether focus is currently within the drawer. */
    private _isFocusWithinDrawer(): boolean {
        const activeEl = this.document.activeElement;
        return !!activeEl && this.elementRef.nativeElement.contains(activeEl);
    }

    ngAfterViewInit() {
        this._isAttached = true;

        // Only update the DOM position when the sidenav is positioned at
        // the end since we project the sidenav before the content by default.
        //if (this.position === 'right') {
        // this.updatePositionInParent();
        //}

        // Needs to happen after the position is updated
        // so the focus trap anchors are in the right place.
        if (this.platform.isBrowser) {
            this.focusTrap = this.focusTrapFactory.create(this.elementRef.nativeElement);
            this._updateFocusTrapState();
        }
    }

    ngAfterContentChecked() {
        // Enable the animations after the lifecycle hooks have run, in order to avoid animating
        // drawers that are open by default. When we're on the server, we shouldn't enable the
        // animations, because we don't want the drawer to animate the first time the user sees
        // the page.
        if (this.platform.isBrowser) {
            this._enableAnimations = true;
        }
    }

    ngOnDestroy() {
        this.focusTrap?.destroy();
        this._anchor?.remove();
        this._anchor = null;
        this._animationStarted.complete();
        this._animationEnd.complete();
        this._modeChanged.complete();
        this._destroyed.next();
        this._destroyed.complete();
    }

    /**
     * Open the drawer.
     * @param openedVia Whether the drawer was opened by a key press, mouse click or programmatically.
     * Used for focus management after the sidenav is closed.
     */
    open(openedVia?: FocusOrigin): Promise<DrawerToggleResult> {
        return this.toggle(true, openedVia);
    }

    /** Close the drawer. */
    close(): Promise<DrawerToggleResult> {
        return this.toggle(false);
    }

    /** Closes the drawer with context that the backdrop was clicked. */
    _closeViaBackdropClick(): Promise<DrawerToggleResult> {
        // If the drawer is closed upon a backdrop click, we always want to restore focus. We
        // don't need to check whether focus is currently in the drawer, as clicking on the
        // backdrop causes blurs the active element.
        return this._setOpen(/* isOpen */ false, /* restoreFocus */ true, 'mouse');
    }

    /**
     * Toggle this drawer.
     * @param isOpen Whether the drawer should be open.
     * @param openedVia Whether the drawer was opened by a key press, mouse click or programmatically.
     * Used for focus management after the sidenav is closed.
     */
    toggle(isOpen: boolean = !this.isOpen, openedVia?: FocusOrigin): Promise<DrawerToggleResult> {
        // If the focus is currently inside the drawer content and we are closing the drawer,
        // restore the focus to the initially focused element (when the drawer opened).
        if (isOpen && openedVia) {
            this._openedVia = openedVia;
        }

        const result = this._setOpen(
            isOpen,
            !isOpen && this._isFocusWithinDrawer(),
            this._openedVia || 'program',
        );

        if (!isOpen) {
            this._openedVia = null;
        }

        return result;
    }

    /**
     * Toggles the opened state of the drawer.
     * @param isOpen Whether the drawer should open or close.
     * @param restoreFocus Whether focus should be restored on close.
     * @param focusOrigin Origin to use when restoring focus.
     */
    private _setOpen(
        isOpen: boolean,
        restoreFocus: boolean,
        focusOrigin: Exclude<FocusOrigin, null>,
    ): Promise<DrawerToggleResult> {
        this.isOpen = isOpen;

        if (isOpen) {
            this._animationState = this._enableAnimations ? 'open' : 'open-instant';
        } else {
            this._animationState = 'void';

        }

        // Needed to ensure that the closing sequence fires off correctly.
        this._changeDetectorRef.markForCheck();
        this._updateFocusTrapState();

        return new Promise<DrawerToggleResult>(resolve => {
            this.openedChange.pipe(take(1)).subscribe(open => resolve(open ? 'open' : 'close'));
        });
    }

    _getWidth(): number {
        return this.elementRef.nativeElement ? this.elementRef.nativeElement.offsetWidth || 0 : 0;
    }

    /** Updates the enabled state of the focus trap. */
    private _updateFocusTrapState() {
        if (this.focusTrap) {
            // Trap focus only if the backdrop is enabled. Otherwise, allow end user to interact with the
            // sidenav content.
            this.focusTrap.enabled = !!this.container?.hasBackdrop && this.isOpen;
        }
    }

    maskClick(): void {
        if (this.maskClosable && this.mask) {
            this.close();
        }
    }
}