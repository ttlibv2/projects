import { Injectable, NgZone, OnDestroy, Renderer2, RendererFactory2 } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { auditTime, finalize } from 'rxjs/operators';

const NOOP = (): void => { };

@Injectable({providedIn: 'root'})
export class ResizeService implements OnDestroy {
    private readonly resizeSource$ = new Subject<void>();

    private listeners = 0;

    private renderer: Renderer2;

    private disposeHandle = NOOP;

    private handler = (): void => {
        this.ngZone.run(() => {
            this.resizeSource$.next();
        });
    };

    constructor(
        private ngZone: NgZone,
        private rendererFactory2: RendererFactory2
    ) {
        this.renderer = this.rendererFactory2.createRenderer(null, null);
    }

    ngOnDestroy(): void {
        // Caretaker note: the `handler` is an instance property (it's not defined on the class prototype).
        // The `handler` captures `this` and prevents the `NzResizeService` from being GC'd.
        this.handler = NOOP;
    }

    subscribe(): Observable<void> {
        this.registerListener();

        return this.resizeSource$.pipe(
            auditTime(16),
            finalize(() => this.unregisterListener())
        );
    }

    unsubscribe(): void {
        this.unregisterListener();
    }

    private registerListener(): void {
        if (this.listeners === 0) {
            this.ngZone.runOutsideAngular(() => {
                this.disposeHandle = this.renderer.listen('window', 'resize', this.handler);
            });
        }

        this.listeners += 1;
    }

    private unregisterListener(): void {
        this.listeners -= 1;

        if (this.listeners === 0) {
            this.disposeHandle();
            this.disposeHandle = NOOP;
        }
    }
}