import { AppendTo } from "./interface";

export interface MenuProps {

    /**
     * Target element to attach the overlay, valid values are "body" or a local ng-template variable of another element (note: use binding with brackets for template variables, e.g. [appendTo]="mydiv" for a div element having #mydiv as variable name).
     * @group Props
     */
    appendTo?: AppendTo;

    /**
    * Whether to automatically manage layering.
    * @group Props
    */
    autoZIndex?: boolean;
    /**
     * Base zIndex value to use in layering.
     * @group Props
     */
    baseZIndex?: number;
    /**
     * Transition options of the show animation.
     * @group Props
     */
    showTransitionOptions?: string;
    /**
     * Transition options of the hide animation.
     * @group Props
     */
    hideTransitionOptions?: string;
    /**
     * Defines a string value that labels an interactive element.
     * @group Props
     */
    ariaLabel?: string | undefined;
    /**
     * Identifier of the underlying input element.
     * @group Props
     */
    ariaLabelledBy?: string | undefined;
    /**
     * Index of the element in tabbing order.
     * @group Props
     */
    tabindex?: number;

    onShow?:() => void;
    onHide?:() => void;
    
}