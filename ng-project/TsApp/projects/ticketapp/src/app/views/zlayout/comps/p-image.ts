import { Directive, ElementRef, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, TemplateRef } from "@angular/core";
import { SafeUrl } from "@angular/platform-browser";
import { Image } from "primeng/image";

export interface PImageOption {
    /**
        * Style class of the image element.
        * @group Props
        */
    imageClass?: string | undefined;
    /**
     * Inline style of the image element.
     * @group Props
     */
    imageStyle?: { [klass: string]: any } | null | undefined;
    /**
     * Class of the element.
     * @group Props
     */
    styleClass?: string | undefined;
    /**
     * Inline style of the element.
     * @group Props
     */
    style?: { [klass: string]: any } | null | undefined;
    /**
     * The source path for the main image.
     * @group Props
     */
    src?: string | SafeUrl | undefined;
    /**
     * The srcset definition for the main image.
     * @group Props
     */
    srcSet?: string | SafeUrl | undefined;
    /**
     * The sizes definition for the main image.
     * @group Props
     */
    sizes?: string | undefined;
    /**
     * The source path for the preview image.
     * @group Props
     */
    previewImageSrc?: string | SafeUrl | undefined;
    /**
     * The srcset definition for the preview image.
     * @group Props
     */
    previewImageSrcSet?: string | SafeUrl | undefined;
    /**
     * The sizes definition for the preview image.
     * @group Props
     */
    previewImageSizes?: string | undefined;
    /**
     * Attribute of the preview image element.
     * @group Props
     */
    alt?: string | undefined;
    /**
     * Attribute of the image element.
     * @group Props
     */
    width?: string | undefined;
    /**
     * Attribute of the image element.
     * @group Props
     */
    height?: string | undefined;
    /**
     * Attribute of the image element.
     * @group Props
     */
    loading?: 'lazy' | 'eager' | undefined;
    /**
     * Target element to attach the dialog, valid values are "body" or a local ng-template variable of another element (note: use binding with brackets for template variables, e.g. [appendTo]="mydiv" for a div element having #mydiv as variable name).
     * @group Props
     */
    appendTo?: HTMLElement | ElementRef | TemplateRef<any> | string | null | undefined | any;
    /**
     * Controls the preview functionality.
     * @group Props
     */
    preview?: boolean;
    /**
     * Transition options of the show animation
     * @group Props
     */
    showTransitionOptions?: string;
    /**
     * Transition options of the hide animation
     * @group Props
     */
    hideTransitionOptions?: string;
    /**
     * Triggered when the preview overlay is shown.
     * @group Emits
     */
    onShow?: () => void;
    /**
     * Triggered when the preview overlay is hidden.
     * @group Emits
     */
    onHide?: () => void;
    /**
     * This event is triggered if an error occurs while loading an image file.
     * @param {Event} event - Browser event.
     * @group Emits
     */
    onImageError?: (event: Event) => void;

}

@Directive({
    standalone: true,
    selector: '[pImage]',
})
export class PImageDirective implements OnChanges {
    @Input() options: PImageOption;

    get src() { return this.options?.src; }

    constructor(private element: Image) { }

    ngOnChanges(changes: SimpleChanges): void {
        this.applyOption();
    }

    private applyOption(): void {
        const opt = this.options || {};

        Object.assign(this.element, this.options);



        if (this.options?.onShow) {
            this.element.onShow.subscribe(_ => this.options.onShow());
        }

        if (this.options?.onHide) {
            this.element.onHide.subscribe(_ => this.options.onHide());
        }

        if (this.options?.onImageError) {
            this.element.onImageError.subscribe(e => this.options.onImageError(e));
        }
    }
}
