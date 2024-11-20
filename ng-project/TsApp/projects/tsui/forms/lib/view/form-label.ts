import { CommonModule } from "@angular/common";
import { booleanAttribute, Component, ElementRef, EmbeddedViewRef, Input, OnChanges, OnDestroy, SimpleChanges, TemplateRef, ViewContainerRef, ViewEncapsulation } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { Objects } from "ts-ui/helper";
const {isNull, isFalse, isString} = Objects;

@Component({
    standalone: true,
    selector: 'ts-label',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule],
    template: `
        @if(icon){<span #iconEl class="label-icon" [ngClass]="icon"></span>}
        @if(html){<span #htmlEl class="html" [innerHTML]="html"></span>}
        @else{<ng-content></ng-content>}

        @if(required){<span class="text-danger bold required">{{required}}</span>}
    `,
    host: {
        '[attr.for]': 'for',
        '[class.ts-label]': 'true'
    }
})
export class FormLabel implements OnChanges, OnDestroy {
    //@Input() content: string | TemplateRef<any>;

    //@Input({ transform: booleanAttribute })
    //istext: boolean | undefined = true;

    /**
     * Defined icon label
     * @groups Props
     * */
    @Input() icon: string;

    /**
     * Defined required label
     * @groups Props
     * */
    @Input() set required(val: string | boolean) {
        this._required = isNull(val) || isFalse(val) ? undefined
            : isString(val) ? val : '(*)';
    }

    get required() { return this._required; }


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
    private _required: string;

    constructor(
        private sanitizer: DomSanitizer,
        private elementRef: ElementRef<HTMLElement>,
        private container: ViewContainerRef) { }


    ngOnChanges(changes: SimpleChanges): void {
    }


    ngOnDestroy(): void {
        this.embeddedView = undefined;
        this.html = undefined;
        this.icon = undefined;
    }
}