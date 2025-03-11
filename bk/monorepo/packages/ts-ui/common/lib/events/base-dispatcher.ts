import { _getEventTarget, Platform } from "@angular/cdk/platform";
import { DOCUMENT } from "@angular/common";
import { inject, Injectable, NgZone, OnDestroy, RendererFactory2 } from "@angular/core";
import { Subject } from "rxjs";

export interface DispatcherElement {
    get keydown(): Subject<KeyboardEvent>;
    get outsideClick(): Subject<MouseEvent>;
    get targetElement(): any;
    get hasAttached(): boolean;
}

@Injectable({ providedIn: 'root' })
export abstract class BaseDispatcher implements OnDestroy {

    /** Currently attached overlays in the order they were attached. */
    protected readonly _attachedElements: DispatcherElement[] = [];
    protected readonly _ngZone = inject(NgZone);
    protected readonly _document = inject(DOCUMENT);

    protected _isAttached: boolean = false;

    /** Add a new overlay to the list of attached overlay refs. */
    add(element: DispatcherElement): void {
        this.remove(element);
        this._attachedElements.push(element);
    }

    /** Remove an overlay from the list of attached overlay refs. */
    remove(element: DispatcherElement): void {
        const index = this._attachedElements.indexOf(element);

        if (index > -1) {
            this._attachedElements.splice(index, 1);
        }

        // Remove the global listener once there are no more overlays.
        if (this._attachedElements.length === 0) {
            this.detach();
        }
    }

    /** Detaches the global event listener. */
    protected abstract detach(): void;

    ngOnDestroy(): void {
        this.detach();
    }

}

/**
 * Service for dispatching keyboard events that land on the body to appropriate overlay ref,
 * if any. It maintains a list of attached overlays to determine best suited overlay based
 * on event target and order of overlay opens.
 */
@Injectable({ providedIn: 'root' })
export class KeyboardDispatcher extends BaseDispatcher {
    private renderer = inject(RendererFactory2).createRenderer(null, null);
    private cleanupKeydown: (() => void) | undefined;

    override add(element: DispatcherElement): void {
        super.add(element);

        // Lazily start dispatcher once first overlay is added
        if (!this._isAttached) {
            this._ngZone.runOutsideAngular(() => {
                this.cleanupKeydown = this.renderer.listen('body', 'keydown', this.keydownListener);
            });

            this._isAttached = true;
        }
    }

    /** Detaches the global keyboard event listener. */
    protected override detach(): void {
        if (this._isAttached) {
            this.cleanupKeydown?.();
            this._isAttached = false;
        }
    }

    /** Keyboard event listener that will be attached to the body. */
    private keydownListener = (event: KeyboardEvent) => {
        const overlays = this._attachedElements;

        for (let i = overlays.length - 1; i > -1; i--) {
            // Dispatch the keydown event to the top overlay which has subscribers to its keydown events.
            // We want to target the most recent overlay, rather than trying to match where the event came
            // from, because some components might open an overlay, but keep focus on a trigger element
            // (e.g. for select and autocomplete). We skip overlays without keydown event subscriptions,
            // because we don't want overlays that don't handle keyboard events to block the ones below
            // them that do.
            if (overlays[i].keydown && overlays[i].keydown.observed) {
                this._ngZone.run(() => overlays[i].keydown.next(event));
                break;
            }
        }
    };
}

/**
 * Service for dispatching mouse click events that land on the body to appropriate overlay ref,
 * if any. It maintains a list of attached overlays to determine best suited overlay based
 * on event target and order of overlay opens.
 */
@Injectable({ providedIn: 'root' })
export class OutsideClickDispatcher extends BaseDispatcher {
    private _platform = inject(Platform);

    private _cursorOriginalValue: string;
    private _cursorStyleIsSet = false;
    private _pointerDownEventTarget: HTMLElement | null;

    /** Add a new overlay to the list of attached overlay refs. */
    override add(overlayRef: DispatcherElement): void {
        super.add(overlayRef);

        // Safari on iOS does not generate click events for non-interactive
        // elements. However, we want to receive a click for any element outside
        // the overlay. We can force a "clickable" state by setting
        // `cursor: pointer` on the document body. See:
        // https://developer.mozilla.org/en-US/docs/Web/API/Element/click_event#Safari_Mobile
        // https://developer.apple.com/library/archive/documentation/AppleApplications/Reference/SafariWebContent/HandlingEvents/HandlingEvents.html
        if (!this._isAttached) {
            const body = this._document.body;

            /** @breaking-change 14.0.0 _ngZone will be required. */
            if (this._ngZone) {
                this._ngZone.runOutsideAngular(() => this._addEventListeners(body));
            } else {
                this._addEventListeners(body);
            }

            // click event is not fired on iOS. To make element "clickable" we are
            // setting the cursor to pointer
            if (this._platform.IOS && !this._cursorStyleIsSet) {
                this._cursorOriginalValue = body.style.cursor;
                body.style.cursor = 'pointer';
                this._cursorStyleIsSet = true;
            }

            this._isAttached = true;
        }
    }

    /** Detaches the global keyboard event listener. */
    protected detach() {
        if (this._isAttached) {
            const body = this._document.body;
            body.removeEventListener('pointerdown', this._pointerDownListener, true);
            body.removeEventListener('click', this._clickListener, true);
            body.removeEventListener('auxclick', this._clickListener, true);
            body.removeEventListener('contextmenu', this._clickListener, true);
            if (this._platform.IOS && this._cursorStyleIsSet) {
                body.style.cursor = this._cursorOriginalValue;
                this._cursorStyleIsSet = false;
            }
            this._isAttached = false;
        }
    }

    private _addEventListeners(body: HTMLElement): void {
        body.addEventListener('pointerdown', this._pointerDownListener, true);
        body.addEventListener('click', this._clickListener, true);
        body.addEventListener('auxclick', this._clickListener, true);
        body.addEventListener('contextmenu', this._clickListener, true);
    }

    /** Store pointerdown event target to track origin of click. */
    private _pointerDownListener = (event: PointerEvent) => {
        this._pointerDownEventTarget = _getEventTarget<HTMLElement>(event);
    };

    /** Click event listener that will be attached to the body propagate phase. */
    private _clickListener = (event: MouseEvent) => {
        const target = _getEventTarget<HTMLElement>(event);
        // In case of a click event, we want to check the origin of the click
        // (e.g. in case where a user starts a click inside the overlay and
        // releases the click outside of it).
        // This is done by using the event target of the preceding pointerdown event.
        // Every click event caused by a pointer device has a preceding pointerdown
        // event, unless the click was programmatically triggered (e.g. in a unit test).
        const origin =
            event.type === 'click' && this._pointerDownEventTarget
                ? this._pointerDownEventTarget : target;

        // Reset the stored pointerdown event target, to avoid having it interfere
        // in subsequent events.
        this._pointerDownEventTarget = null;

        // We copy the array because the original may be modified asynchronously if the
        // outsidePointerEvents listener decides to detach overlays resulting in index errors inside
        // the for loop.
        const overlays = this._attachedElements.slice();

        // Dispatch the mouse event to the top overlay which has subscribers to its mouse events.
        // We want to target all overlays for which the click could be considered as outside click.
        // As soon as we reach an overlay for which the click is not outside click we break off
        // the loop.
        for (let i = overlays.length - 1; i > -1; i--) {
            const overlayRef = overlays[i], hasClick = overlayRef.outsideClick && !overlayRef.outsideClick.observed;
            if (hasClick || overlays[i].hasAttached) {
                continue;
            }

            // If it's a click inside the overlay, just break - we should do nothing
            // If it's an outside click (both origin and target of the click) dispatch the mouse event,
            // and proceed with the next overlay
            if (
                containsPierceShadowDom(overlayRef.targetElement, target) ||
                containsPierceShadowDom(overlayRef.targetElement, origin)
            ) {
                break;
            }


            if (overlayRef.outsideClick) {
                const outsidePointerEvents = overlayRef.outsideClick;
                if (this._ngZone) {
                    this._ngZone.run(() => outsidePointerEvents.next(event));
                } else {
                    outsidePointerEvents.next(event);
                }
            }
        }
    };

}


/** Version of `Element.contains` that transcends shadow DOM boundaries. */
function containsPierceShadowDom(parent: HTMLElement, child: HTMLElement | null): boolean {
    const supportsShadowRoot = typeof ShadowRoot !== 'undefined' && ShadowRoot;
    let current: Node | null = child;

    while (current) {
        if (current === parent) {
            return true;
        }

        current =
            supportsShadowRoot && current instanceof ShadowRoot ? current.host : current.parentNode;
    }

    return false;
}
