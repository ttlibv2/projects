import { Directive, ElementRef, EventEmitter, NgZone, OnDestroy, OnInit, Output } from "@angular/core";

export class ResizedEvent {
    public newRect: DOMRectReadOnly;
    public oldRect?: DOMRectReadOnly;
    public isFirst: boolean;

    public constructor(newRect: DOMRectReadOnly, oldRect: DOMRectReadOnly | undefined) {
        this.newRect = newRect;
        this.oldRect = oldRect;
        this.isFirst = oldRect == null;
    }
}

@Directive({
    selector: '[resized]',
    standalone: true
})
export class ResizedDirective implements OnInit, OnDestroy {
    private observer: ResizeObserver;
    private oldRect?: DOMRectReadOnly;

    @Output()
    readonly resized = new EventEmitter<ResizedEvent>();;

    public constructor(
        private readonly element: ElementRef,
        private readonly zone: NgZone) {
        this.observer = new ResizeObserver(entries => this.zone.run(() => this.observe(entries)));
    }

    ngOnInit(): void {
        this.observer.observe(this.element.nativeElement)
    }

    ngOnDestroy(): void {
        this.observer.disconnect();
    }

    private observe(entries: ResizeObserverEntry[]): void {
        const domSize = entries[0];
        const resizedEvent = new ResizedEvent(domSize.contentRect, this.oldRect);
        this.oldRect = domSize.contentRect;
        this.resized.emit(resizedEvent);
    }
}