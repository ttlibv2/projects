import { inject, Injectable, NgZone, OnDestroy, Renderer2, RendererFactory2 } from "@angular/core";
import { auditTime, finalize, Observable, Subject } from "rxjs";

type Handler = () => void;
const NOOP: Handler = (): void => { };

@Injectable({ providedIn: 'root' })
export class ResizeObserver implements OnDestroy {
    private readonly source$ = new Subject<void>();
    private readonly ngZone = inject(NgZone);
    private readonly renderer: Renderer2;
    private disposeHandler: Handler = NOOP;
    private listeners: number = 0;

    private handler = (): void => {
        this.ngZone.run(() => this.source$.next());
    };

    constructor(rendererFac: RendererFactory2) {
        this.renderer = rendererFac.createRenderer(null, null);
    }

    observe(): Observable<void> {
        this.registerListener();
        return this.source$.pipe(
            auditTime(16),
            finalize(() => this.unregisterListener())
        );
    }

    unsubscribe(): void {
        this.unregisterListener();
    }

    ngOnDestroy(): void {
        this.unsubscribe();
        this.handler = NOOP;
        this.source$.complete();
        this.listeners = 0;
    }

    private registerListener(): void {
        if(this.listeners === 0) {
            this.ngZone.runOutsideAngular(() => {
                this.disposeHandler = this.renderer.listen('window', 'resize', this.handler);
            });
        }
        this.listeners += 1;
    }

    private unregisterListener(): void {
        this.listeners -= 1;
        if (this.listeners === 0) {
            this.disposeHandler();
            this.disposeHandler = NOOP;
        }
    }

}