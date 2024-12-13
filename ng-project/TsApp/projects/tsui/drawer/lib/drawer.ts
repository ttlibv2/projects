import { CdkScrollable } from "@angular/cdk/scrolling";
import { ESCAPE, hasModifierKey } from '@angular/cdk/keycodes';
import { FocusMonitor, FocusOrigin, FocusTrap, FocusTrapFactory, InteractivityChecker } from "@angular/cdk/a11y";
import { CommonModule, DOCUMENT } from "@angular/common";
import { AfterContentChecked, AfterViewInit, booleanAttribute, ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, EventEmitter, inject, Injector, Input, NgZone, numberAttribute, OnDestroy, OnInit, Output, ViewEncapsulation } from "@angular/core";
import { DrawerMode, DrawerPosition } from "./idrawer";
import { INgStyle } from "ts-ui/common";
import { filter, fromEvent, map, Observable, Subject, takeUntil } from "rxjs";
import { AnimationEvent } from "@angular/animations";
import { Platform } from "@angular/cdk/platform";
import { drawerAnimations } from "./drawer-animations";

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CommonModule, CdkScrollable],
    animations: [drawerAnimations.transformDrawer],
    selector: 'ts-drawer',
    styleUrl: 'drawer.scss',
    templateUrl: 'drawer.html',
    host: { 
        'class': 'ts-drawer',
        '[class.ts-drawer-left]':  `position === 'left'`,
        '[class.ts-drawer-right]': `position === 'right'`,
        '[class.ts-drawer-top]': `position === 'top'`,
        '[class.ts-drawer-bottom]': `position === 'bottom'`,
        '[class.ts-drawer-over]': `mode === 'over'`,
        '[class.ts-drawer-side]': `mode === 'side'`,
        '[class.ts-drawer-push]': `mode === 'push'`,
        '[style.zindex]': 'zindex',
        '[@transform]': '_animationState',
        '(@transform.start)': '_animationStarted.next($event)',
        '(@transform.done)': '_animationEnd.next($event)',
    }
})
export class Drawer implements OnInit, AfterViewInit, AfterContentChecked, OnDestroy {
   /** Emits whenever the drawer has started animating. */
    private readonly _animationStarted = new Subject<AnimationEvent>();

    /** Emits whenever the drawer is done animating. */
    private readonly _animationEnd = new Subject<AnimationEvent>();

    /** Current state of the sidenav animation. */
    private _animationState: 'open-instant' | 'open' | 'void' = 'void';

    /** Emits when the component is destroyed. */
    private readonly _destroyed = new Subject<void>();

    private _opened: boolean = false;

    /** How the sidenav was opened (keypress, mouse click etc.) */
    private _openedVia: FocusOrigin | null;    

    /** Whether the drawer is initialized. Used for disabling the initial animation. */
    private _enableAnimations = false;

    /** Whether the view of the component has been attached. */
    private _isAttached: boolean;

    /** Anchor node used to restore the drawer to its initial position. */
    private _anchor: Comment | null;

    /**
     * An observable that emits when the drawer mode changes. This is used by the drawer container to
     * to know when to when the mode changes so it can adapt the margins on the content.
     */
    readonly _modeChanged = new Subject<void>();

    /**
     * The side that the drawer is attached to.
     * @group Props 
     * */
    @Input() position: DrawerPosition = 'left';

    /**
     * Mode of the drawer; one of 'over', 'push' or 'side'.
     * @group Props 
     * */
    @Input() mode: DrawerMode = 'push';

    /**
     * /**
   * Whether the drawer is opened. We overload this because we trigger an event when it
   * starts or end.
   */
    @Input({ transform: booleanAttribute }) opened: boolean;

    /**
     * Whether the sidenav is fixed in the viewport.
     * @group Props 
     * */
    @Input({ transform: booleanAttribute }) fixedInViewport: boolean;

    /**
     * The gap between the bottom of the sidenav and the bottom of the viewport when the sidenav is in fixed mode.
     * Apply when `fixedInViewport = true`
     * @group Props 
     * */
    @Input({ transform: numberAttribute }) fixedBottomGap: number

    /**
     * The gap between the top of the sidenav and the top of the viewport when the sidenav is in fixed mode.
     * Apply when `fixedInViewport = true`
     * @group Props 
     * */
    @Input({ transform: numberAttribute }) fixedTopGap: number

    /** Whether to show mask or not. */
    @Input({ transform: booleanAttribute }) mask: boolean = true;

    /** Clicking on the mask (area outside the Drawer) to close the Drawer or not. */
    @Input({ transform: booleanAttribute }) maskClosable: boolean = true;

    /** Whether support press esc to close */
    @Input({ transform: booleanAttribute }) keyboard: boolean = true;

    /** Style for Drawer's mask element. */
    @Input() maskStyle: INgStyle;

    /** class for Drawer's mask element. */
    @Input() maskClass: string;

    /**Width of the Drawer dialog, only when placement is 'right' or 'left', having a higher priority than nzSize. */
    @Input() width: number | string;

    /** Height of the Drawer dialog, only when placement is 'top' or 'bottom', having a higher priority than nzSize. */
    @Input() height: number | string;

    /**The X coordinate offset(px), only when placement is 'right' or 'left'. */
    @Input({ transform: numberAttribute }) offsetX: number;

    /**The Y coordinate offset(px), only when placement is 'top' or 'bottom'. */
    @Input({ transform: numberAttribute }) offsetY: number;

    /**	The z-index of the Drawer. */
    @Input({ transform: numberAttribute }) zindex: number;

    /** Event emitted when the drawer open state is changed. */
    @Output() readonly openedChange = new EventEmitter<boolean>(true);

    /** Event emitted when the drawer has started opening. */
    @Output() readonly openedStart: Observable<void> = this._animationStarted.pipe(
        filter(e => e.fromState !== e.toState && e.toState.startsWith('open')), map(() => undefined)
    );

    /** 
     * Event emitted when the drawer has been opened. 
     * @_openedStream
     * */
    @Output() readonly afterOpen = this.openedChange.pipe(
        filter(o => o), map(() => { })
    );

    /** 
     * Event emitted when the drawer has been closed. 
     * @_closedStream
     * */
    @Output() readonly afterClose = this.openedChange.pipe(
        filter(o => !o), map(() => { })
    );

    /** Event emitted when the drawer has started closing. */
    @Output() readonly closedStart: Observable<void> = this._animationStarted.pipe(
        filter(e => e.fromState !== e.toState && e.toState === 'void'), map(undefined)
    );

    /** Event emitted when the drawer's position changes. */
    @Output() readonly positionChanged = new EventEmitter<DrawerPosition>();
    
    //++++++++++++++++++++++++++++++++++++++++

    private _injector = inject(Injector);
    private _changeDetectorRef = inject(ChangeDetectorRef);

    private _elementRef = inject(ElementRef<HTMLElement>);
    private _focusTrapFactory = inject(FocusTrapFactory);
    private _focusMonitor = inject(FocusMonitor);
    private _platform = inject(Platform);
    private _ngZone = inject(NgZone);
    private _interactivityChecker = inject(InteractivityChecker);
    private _doc = inject(DOCUMENT, { optional: true })!;
    private _focusTrap: FocusTrap | null = null;
    private _elementFocusedBeforeDrawerWasOpened: HTMLElement | null = null;

    //++++++++++++++++++++++++++++++++++++++++

    ngOnInit(): void {
       // this.openedChange.pipe(takeUntil(this._destroyed)).subscribe((opened: boolean) => {  })


        /**
         * Listen to `keydown` events outside the zone so that change detection is not run every
         * time a key is pressed. Instead we re-enter the zone only if the `ESC` key is pressed
         * and we don't have close disabled.
         */
        this._ngZone.runOutsideAngular(() => {
            // (fromEvent(this._elementRef.nativeElement, 'keydown') as Observable<KeyboardEvent>)
            //     .pipe(
            //         filter(event => {
            //             return event.code === ESCAPE && !this.maskClosable && !hasModifierKey(event);
            //         }),
            //         takeUntil(this._destroyed),
            //     )
            //     .subscribe(event =>
            //         this._ngZone.run(() => {
            //             this.close();
            //             event.stopPropagation();
            //             event.preventDefault();
            //         }),
            //     );
        });

       this._animationEnd.subscribe(event => {
           const { fromState, toState } = event;
           if (
               (toState.indexOf('open') === 0 && fromState === 'void') ||
               (toState === 'void' && fromState.indexOf('open') === 0)
           ) {
               this.openedChange.emit(this._opened);
           }
       });


    }
 






    ngAfterViewInit(): void {

    }

    ngAfterContentChecked(): void {

    }

    ngOnDestroy(): void {

    }

    private changePosition(oldPos: DrawerPosition) {
        if (this.position !== oldPos) {

            if (this._isAttached) {
                //this._updatePositionInParent(value);
            }

            this.positionChanged.emit(this.position);
        }
    }

    private changeMode(oldMode: DrawerMode) {
        if(this.mode !== oldMode){
        //this._updateFocusTrapState();
        this._modeChanged.next();}
    }
}