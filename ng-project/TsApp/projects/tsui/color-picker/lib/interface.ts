import { ElementRef, TemplateRef } from "@angular/core";
import { Color } from "./color";

export type ColorFormatType = 'rgb' | 'hex' | 'hsb';


export interface TransformOffset {
    x: number;
    y: number;
}

export interface CopyColorEvent {
    color: string;
    origin: Color;
    format: ColorFormatType;
}

export interface ChangeFormatEvent {
    format: ColorFormatType;
    color: string;
    origin: Color;
}


export const OVERLAY_OPTION_DEFAULT: OverlayOptions = {
    dismissable: true,
    showCloseIcon: false,
    appendTo: 'body',
    autoZIndex: true,
    baseZIndex: 0,
    focusOnShow: true,
    showTransitionOptions: '.12s cubic-bezier(0, 0, 0.2, 1)',
    hideTransitionOptions: '.1s linear',
    onShow: _ => {}, onHide: _ => {}
};

export interface OverlayOptions {
    /**
    * Defines a string that labels the input for accessibility.
    * @group Props
    */
    ariaLabel?:string | undefined;
    /**
     * Establishes relationships between the component and label(s) where its value should be one or more element IDs.
     * @group Props
     */
    ariaLabelledBy?:string | undefined;
    /**
     * Enables to hide the overlay when outside is clicked.
     * @group Props
     */
    dismissable?:boolean;
    /**
     * When enabled, displays a close icon at top right corner.
     * @group Props
     */
    showCloseIcon?:boolean | undefined;
    /**
     * Inline style of the component.
     * @group Props
     */
    style?:{ [klass: string]: any } | null | undefined;
    /**
     * Style class of the component.
     * @group Props
     */
    styleClass?:string | undefined;
    /**
     *  Target element to attach the panel, valid values are "body" or a local ng-template variable of another element (note: use binding with brackets for template variables, e.g. [appendTo]="mydiv" for a div element having #mydiv as variable name).
     * @group Props
     */
    appendTo?:HTMLElement | ElementRef | TemplateRef<any> | string | null | undefined | any;
    /**
     * Whether to automatically manage layering.
     * @group Props
     */
    autoZIndex?:boolean;
    /**
     * Aria label of the close icon.
     * @group Props
     */
    ariaCloseLabel?:string | undefined;
    /**
     * Base zIndex value to use in layering.
     * @group Props
     */
    baseZIndex?:number;
    /**
     * When enabled, first button receives focus on show.
     * @group Props
     */
    focusOnShow?:boolean;
    /**
     * Transition options of the show animation.
     * @group Props
     */
    showTransitionOptions?:string;
    /**
     * Transition options of the hide animation.
     * @group Props
     */
    hideTransitionOptions?:string;

    /**
     * A callback function that is invoked when the overlay is shown.
     */
    onShow?:(event: any) => void;

    /**
     * Callback to invoke when an overlay gets hidden.
     * @group Emits
     */
    onHide?: (event: any) => void;
}