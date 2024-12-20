import { debounceTime, filter, merge, startWith, Subject, takeUntil } from "rxjs";
import { Drawer } from "./drawer";
import { AfterContentInit, afterNextRender, ANIMATION_MODULE_TYPE, booleanAttribute, ChangeDetectionStrategy, ChangeDetectorRef, Component, ContentChild, ContentChildren, DoCheck, ElementRef, EventEmitter, inject, InjectionToken, Injector, Input, NgZone, OnDestroy, Output, QueryList, ViewChild, ViewEncapsulation } from "@angular/core";
import { AnimationEvent } from "@angular/animations";
import { CdkScrollable, ViewportRuler } from "@angular/cdk/scrolling";
import { BooleanInput, coerceBooleanProperty } from "@angular/cdk/coercion";
import { Directionality } from "@angular/cdk/bidi";
import { ContentMargin, DRAWER_CONTAINER, DRAWER_DEFAULT_AUTOSIZE } from "./drawer.interface";
import { DrawerContent } from "./drawer-content";
import { DomHandler } from "ts-ui/common";

@Component({
    standalone: true, 
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [DrawerContent],
    selector: 'ts-drawer-container', 
    templateUrl: 'drawer-container.html',
    styleUrl: 'drawer.scss',
    host: {
        'class': 'ts-drawer-container',
        //'[class.ts-drawer-container-explicit-backdrop]': '_backdropOverride',
    },
    providers: [
        {
            provide: DRAWER_CONTAINER,
            useExisting: DrawerContainer,
        },
    ]
})
export class DrawerContainer implements AfterContentInit, DoCheck, OnDestroy {
 
    /**
     * Whether to automatically resize the container whenever
     * the size of any of its drawers changes.
     */
    @Input({ transform: booleanAttribute }) autosize: boolean;

    /**
     * Whether the drawer container should have a backdrop while one of the sidenavs is open.
     * If explicitly set to `true`, the backdrop will be enabled for drawers in the `side`
     * mode as well.
     */
    @Input()
    get hasBackdrop(): boolean {
        return this._drawerHasBackdrop(this.leftDrawer) || this._drawerHasBackdrop(this.rightDrawer);
    }
    set hasBackdrop(value: BooleanInput) {
        this._backdropOverride = value == null ? null : coerceBooleanProperty(value);
    }
    _backdropOverride: boolean | null;

    /** Event emitted when the drawer backdrop is clicked. */
    @Output() readonly backdropClick: EventEmitter<void> = new EventEmitter<void>();

    /** The drawer at the start/end position, independent of direction. */
    private leftDrawer: Drawer | null;
    private rightDrawer: Drawer | null;
    private topDrawer: Drawer | null;
    private bottomDrawer: Drawer | null;
    
    /**
     * The drawer at the left/right. When direction changes, these will change as well.
     * They're used as aliases for the above to set the left/right style properly.
     * In LTR, _left == _start and _right == _end.
     * In RTL, _left == _end and _right == _start.
     */
    private _left: Drawer | null;
    private _right: Drawer | null;

    /** Emits when the component is destroyed. */
    private readonly destroyed = new Subject<void>();

    /** Emits on every ngDoCheck. Used for debouncing reflows. */
    private readonly doCheckSubject = new Subject<void>();

    /**
     * Margins to be applied to the content. These are used to push / shrink the drawer content when a
     * drawer is open. We use margin rather than transform even for push mode because transform breaks
     * fixed position elements inside of the transformed element.
     */
    contentMargins: ContentMargin = { left: null, right: null, top: null, bottom: null };

    readonly contentMarginChanges = new Subject<ContentMargin>();

    /** Reference to the CdkScrollable instance that wraps the scrollable content. */
    get scrollable(): CdkScrollable {
        return this.userContent || this.content;
    }

    /** All drawers in the container. Includes drawers from inside nested containers. */
    @ContentChildren(Drawer, { descendants: true, })
    allDrawers: QueryList<Drawer>;

    /** Drawers that belong to this container. */
    drawers = new QueryList<Drawer>();

    @ContentChild(DrawerContent) content: DrawerContent;
    @ViewChild(DrawerContent) userContent: DrawerContent;


    private injector = inject(Injector);

    private readonly dir = inject(Directionality, { optional: true });
    private readonly element = inject(ElementRef<HTMLElement>);
    private readonly ngZone = inject(NgZone);
    private readonly changeDetectorRef = inject(ChangeDetectorRef);
    private readonly animationMode = inject(ANIMATION_MODULE_TYPE, { optional: true });

    /** The drawer child with the `start` position. */
    get left(): Drawer | null { return this.leftDrawer; }

    /** The drawer child with the `end` position. */
    get right(): Drawer | null { return this.rightDrawer; }

    /** The drawer child with the `start` position. */
    get top(): Drawer | null { return this.topDrawer; }

    /** The drawer child with the `end` position. */
    get bottom(): Drawer | null { return this.bottomDrawer; }



    constructor(...args: unknown[]);

    constructor() {
        this.autosize = inject(DRAWER_DEFAULT_AUTOSIZE);

        const viewportRuler = inject(ViewportRuler);

        // If a `Dir` directive exists up the tree, listen direction changes
        // and update the left/right properties to point to the proper start/end.
        this.dir?.change.pipe(takeUntil(this.destroyed)).subscribe(() => {
            this._validateDrawers();
            this.updateContentMargins();
        });

        // Since the minimum width of the sidenav depends on the viewport width,
        // we need to recompute the margins if the viewport changes.
        viewportRuler
            .change()
            .pipe(takeUntil(this.destroyed))
            .subscribe(() => this.updateContentMargins());
    }

    ngAfterContentInit() {
        this.allDrawers.changes
            .pipe(startWith(this.allDrawers), takeUntil(this.destroyed))
            .subscribe((drawer: QueryList<Drawer>) => {
                this.drawers.reset(drawer.filter(item => !item.container || item.container === this));
                this.drawers.notifyOnChanges();
            });

        this.drawers.changes.pipe(startWith(null)).subscribe(() => {
            this._validateDrawers();

            this.drawers.forEach((drawer: Drawer) => {
                this._watchDrawerToggle(drawer);
                this._watchDrawerPosition(drawer);
                this._watchDrawerMode(drawer);
            });

            if (
                !this.drawers.length ||
                this.isDrawerOpen(this.leftDrawer) ||
                this.isDrawerOpen(this.rightDrawer)
            ) {
                this.updateContentMargins();
            }

            this.changeDetectorRef.markForCheck();
        });

        // Avoid hitting the NgZone through the debounce timeout.
        this.ngZone.runOutsideAngular(() => {
            this.doCheckSubject
                .pipe(
                    debounceTime(10), // Arbitrary debounce time, less than a frame at 60fps
                    takeUntil(this.destroyed),
                )
                .subscribe(() => this.updateContentMargins());
        });
    }

    ngOnDestroy() {
        this.contentMarginChanges.complete();
        this.doCheckSubject.complete();
        this.drawers.destroy();
        this.destroyed.next();
        this.destroyed.complete();
    }

    /** Calls `open` of both start and end drawers */
    open(): void {
        this.drawers.forEach(drawer => drawer.open());
    }

    /** Calls `close` of both start and end drawers */
    close(): void {
        this.drawers.forEach(drawer => drawer.close());
    }

    /**
     * Recalculates and updates the inline styles for the content. Note that this should be used
     * sparingly, because it causes a reflow.
     */
    updateContentMargins() {
        // 1. For drawers in `over` mode, they don't affect the content.
        // 2. For drawers in `side` mode they should shrink the content. We do this by adding to the
        //    left margin (for left drawer) or right margin (for right the drawer).
        // 3. For drawers in `push` mode the should shift the content without resizing it. We do this by
        //    adding to the left or right margin and simultaneously subtracting the same amount of
        //    margin from the other side.
        let left = 0;
        let right = 0;

        if (this._left && this._left.isOpen) {
            if (this._left.mode == 'side') {
                left += this._left._getWidth();
            } else if (this._left.mode == 'push') {
                const width = this._left._getWidth();
                left += width;
                right -= width;
            }
        }

        if (this._right && this._right.isOpen) {
            if (this._right.mode == 'side') {
                right += this._right._getWidth();
            } else if (this._right.mode == 'push') {
                const width = this._right._getWidth();
                right += width;
                left -= width;
            }
        }

        // If either `right` or `left` is zero, don't set a style to the element. This
        // allows users to specify a custom size via CSS class in SSR scenarios where the
        // measured widths will always be zero. Note that we reset to `null` here, rather
        // than below, in order to ensure that the types in the `if` below are consistent.
        left = left || null!;
        right = right || null!;

        if (left !== this.contentMargins.left || right !== this.contentMargins.right) {
            this.contentMargins = { left, right };

            // Pull back into the NgZone since in some cases we could be outside. We need to be careful
            // to do it only when something changed, otherwise we can end up hitting the zone too often.
            this.ngZone.run(() => this.contentMarginChanges.next(this.contentMargins));
        }
    }

    ngDoCheck() {
        // If users opted into autosizing, do a check every change detection cycle.
        if (this.autosize && this._isPushed()) {
            // Run outside the NgZone, otherwise the debouncer will throw us into an infinite loop.
            this.ngZone.runOutsideAngular(() => this.doCheckSubject.next());
        }
    }

    /**
     * Subscribes to drawer events in order to set a class on the main container element when the
     * drawer is open and the backdrop is visible. This ensures any overflow on the container element
     * is properly hidden.
     */
    private _watchDrawerToggle(drawer: Drawer): void {
        drawer._animationStarted
            .pipe(
                filter((event: AnimationEvent) => event.fromState !== event.toState),
                takeUntil(this.drawers.changes),
            )
            .subscribe((event: AnimationEvent) => {
                // Set the transition class on the container so that the animations occur. This should not
                // be set initially because animations should only be triggered via a change in state.
                if (event.toState !== 'open-instant' && this.animationMode !== 'NoopAnimations') {
                    this.element.nativeElement.classList.add('ts-drawer-transition');
                }

                this.updateContentMargins();
                this.changeDetectorRef.markForCheck();
            });

        if (drawer.mode !== 'side') {
            drawer.openedChange
                .pipe(takeUntil(this.drawers.changes))
                .subscribe(() => this.setContainerClass(drawer.isOpen));
        }
    }

    /**
     * Subscribes to drawer onPositionChanged event in order to
     * re-validate drawers when the position changes.
     */
    private _watchDrawerPosition(drawer: Drawer): void {
        if (!!drawer) {
            // NOTE: We need to wait for the microtask queue to be empty before validating,
            // since both drawers may be swapping positions at the same time.
            drawer.onPositionChanged.pipe(takeUntil(this.drawers.changes)).subscribe(() => {
                afterNextRender(() => { this._validateDrawers(); }, { injector: this.injector });
            });
        }
    }

    /** Subscribes to changes in drawer mode so we can run change detection. */
    private _watchDrawerMode(drawer: Drawer): void {
        if (drawer) {
            drawer._modeChanged
                .pipe(takeUntil(merge(this.drawers.changes, this.destroyed)))
                .subscribe(() => {
                    this.updateContentMargins();
                    this.changeDetectorRef.markForCheck();
                });
        }
    }

    /** Toggles the 'ts-drawer-opened' class on the main 'ts-drawer-container' element. */
    private setContainerClass(isAdd: boolean): void {
        const className = 'ts-drawer-container-has-open';
        DomHandler.editClass(this.element, isAdd, className);;
    }

    /** Validate the state of the drawer children components. */
    private _validateDrawers() {
        this.leftDrawer = this.rightDrawer = null;

        // Ensure that we have at most one start and one end drawer.
        this.drawers.forEach(drawer => {
            if (drawer.position == 'right') {
                // if (this._end != null && (typeof ngDevMode === 'undefined' || ngDevMode)) {
                //     throwMatDuplicatedDrawerError('end');
                // }
                this.rightDrawer = drawer;
            } else {
                // if (this._start != null && (typeof ngDevMode === 'undefined' || ngDevMode)) {
                //     throwMatDuplicatedDrawerError('start');
                // }
                this.leftDrawer = drawer;
            }
        });

        this._right = this._left = null;

        // Detect if we're LTR or RTL.
        if (this.dir && this.dir.value === 'rtl') {
            this._left = this.rightDrawer;
            this._right = this.leftDrawer;
        } else {
            this._left = this.leftDrawer;
            this._right = this.rightDrawer;
        }
    }

    /** Whether the container is being pushed to the side by one of the drawers. */
    private _isPushed() {
        return (
            (this.isDrawerOpen(this.leftDrawer) && this.leftDrawer.mode != 'over') ||
            (this.isDrawerOpen(this.rightDrawer) && this.rightDrawer.mode != 'over')
        );
    }

    _onBackdropClicked() {
        this.backdropClick.emit();
        this._closeModalDrawersViaBackdrop();
    }

    _closeModalDrawersViaBackdrop() {
        // Close all open drawers where closing is not disabled and the mode is not `side`.
        [this.leftDrawer, this.rightDrawer]
            .filter(drawer => drawer && !drawer.disableClose && this._drawerHasBackdrop(drawer))
            .forEach(drawer => drawer!._closeViaBackdropClick());
    }

    isShowingBackdrop(): boolean {
        return (
            (this.isDrawerOpen(this.leftDrawer) && this._drawerHasBackdrop(this.leftDrawer)) ||
            (this.isDrawerOpen(this.rightDrawer) && this._drawerHasBackdrop(this.rightDrawer))
        ); 
    }

    private isDrawerOpen(drawer: Drawer | null): drawer is Drawer {
        return !!drawer?.isOpen;
    }

    // Whether argument drawer should have a backdrop when it opens
    private _drawerHasBackdrop(drawer: Drawer | null) {
        if (this._backdropOverride == null) {
            return !!drawer && drawer.mode !== 'side';
        }

        return this._backdropOverride;
    }
}