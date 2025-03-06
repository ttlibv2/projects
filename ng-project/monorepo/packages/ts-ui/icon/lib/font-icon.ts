import { CommonModule } from "@angular/common";
import { AfterViewChecked, booleanAttribute, Component, ElementRef, ErrorHandler, EventEmitter, Input, numberAttribute, OnDestroy, OnInit, Output, ViewEncapsulation } from "@angular/core";

@Component({
    standalone: true,
    selector: 'ts-icon',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule],
    template: `<ng-content></ng-content>`,
    host: {
        '[class]': 'name',
        '[class.pi-spin]': 'spin === true',
        '[style.color]': 'color',
        '[style.font-size]': 'size',
        '(click)': 'onClick.emit($event)'
    }
})
export class Icon implements OnInit, AfterViewChecked, OnDestroy {
    private _object: any = {};

    @Input() set color(c: string) {
        this._object.color = c;
    }

    @Input() set name(n: string) {
        this._object.name = n;
    }

    @Input() set size(s: string) {
        this._object.size = s;
    }

    @Input({ transform: booleanAttribute })
    set spin(s: boolean) {
        this._object.spin = s;
    }

    /**
     * Callback to execute when button is clicked.
     * This event is intended to be used with the <p-button> component. Using a regular <button> element, use (click).
     * @param {MouseEvent} event - Mouse event.
     * @group Emits
     */
    @Output() onClick = new EventEmitter<MouseEvent>();

    get color(): string { return this._object.color ?? '#000'; }
    get size(): string { return this._object.size ?? '1rem'; }
    get name(): string { return this._object.name ?? undefined; }
    get spin(): boolean { return this._object.spin ?? false; }

    constructor(
        private elementRef: ElementRef<any>,
        private errorHandle: ErrorHandler) { }

    ngOnInit(): void {
    }

    ngAfterViewChecked(): void {
    }

    ngOnDestroy(): void {
    }
}