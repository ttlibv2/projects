import { booleanAttribute, Component, ElementRef, EmbeddedViewRef, Input, OnChanges, OnDestroy, Renderer2, SecurityContext, SimpleChanges, TemplateRef, ViewContainerRef, ViewEncapsulation } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { Objects } from "ts-ui/helper";
const {isString} = Objects;

@Component({
    standalone: true,
    selector: 'ts-label',
    encapsulation: ViewEncapsulation.None,
    template: `
        <ng-content></ng-content>
    `,
    host: {
        '[attr.for]': 'for',
    }
})
export class FormLabel implements OnChanges, OnDestroy {
    @Input() content: string | TemplateRef<any>;

    @Input({ transform: booleanAttribute })
    istext: boolean | undefined = true;

    /**
     * Defined attribute for
     * @groups Props
     * */
    @Input() for: string;

    /**
     * Defined html
     * @groups Props
     * */
    @Input() html: string | TemplateRef<any>;

    private embeddedView: EmbeddedViewRef<any>;

    constructor(
        private sanitizer: DomSanitizer,
        private elementRef: ElementRef<HTMLElement>,
        private container: ViewContainerRef) {}


    ngOnChanges(changes: SimpleChanges): void {
        const {html} = changes;

        // reset_html
        this.resetHtml();

        if(html && this.html) {
            if(isString(this.html)) this.innerHTML(this.html);
            else this.container.createEmbeddedView(this.html);
        }
    }

    private innerHTML(html?: string): void {
        this.elementRef.nativeElement.innerHTML = !!html ? this.sanitizer.sanitize(SecurityContext.HTML, html) : '';
    }

    private resetHtml(): void {
        this.innerHTML('');
        this.embeddedView?.destroy();
    }

    ngOnDestroy(): void {
        this.resetHtml();
        this.embeddedView = undefined;
        this.html = undefined;
    }
}