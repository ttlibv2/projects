import { CommonModule } from "@angular/common";
import { Component, ElementRef, Input, NgZone, Renderer2, RendererStyleFlags2 } from "@angular/core";

@Component({
    standalone: true,
    selector: 'ts-app-loader',
    imports: [CommonModule],
    template: `
        <div class="loader">
            <ng-content></ng-content>
        </div>
    `,
    host: {
        'class': 'ts-loader',
        '[class.ts-loader-whirly]': `type === 'whirly'`,
        '[class.ts-loader-lagos]': `type === 'lagos'`
    }
})
export class AppLoader {
    @Input() color: string = '#0d6efd';
    @Input() type: 'whirly' | 'lagos' = 'lagos';

    constructor(
        private ngZone: NgZone,
        private renderer: Renderer2,
        private el: ElementRef) {
        console.log(`AppLoader`, this.el.nativeElement)
    }

    /**
     * Implement this callback to set a CSS style for an element in the DOM.
     * @param style The name of the style.
     * @param value The new value.
     * @param flags Flags for style variations. No flags are set by default.
     */
    setStyle(style: string, value: any, flags?: RendererStyleFlags2): void {
        this.ngZone.runOutsideAngular(() => {
            this.renderer.setStyle(this.el.nativeElement, style, value, flags);
        })
        this.renderer.setStyle(this.el.nativeElement, style, value, flags);
    }

    visible(visible: boolean): void {
        this.setStyle('display', visible ? 'flex' : 'none');
    }
}