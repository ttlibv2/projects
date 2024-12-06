import { ElementRef, TemplateRef } from "@angular/core";

export type TooltipPos = 'right' | 'left' | 'top' | 'bottom';

export interface TooltipProps {
    
    /**
     * Position of the tooltip.
     * @group Props
     */
    tooltipPosition?: TooltipPos;

    /**
     * Event to show the tooltip.
     * @group Props
     */
    tooltipEvent?: 'hover' | 'focus' | 'both' | string | any;

    /**
     *  Target element to attach the overlay, valid values are "body", "target" or a local ng-F variable of another element (note: use binding with brackets for template variables, e.g. [appendTo]="mydiv" for a div element having #mydiv as variable name).
     * @group Props
     */
    appendTo?: HTMLElement | ElementRef | TemplateRef<any> | string | null | undefined | any;

    /**
     * Type of CSS position.
     * @group Props
     */
    positionStyle?: string | undefined;

    /**
     * Style class of the tooltip.
     * @group Props
     */
    tooltipStyleClass?: string | undefined;

    /**
     * Whether the z-index should be managed automatically to always go on top or have a fixed value.
     * @group Props
     */
    tooltipZIndex?: string | undefined;

    /**
     * By default the tooltip contents are rendered as text. Set to false to support html tags in the content.
     * @group Props
     */
    escape?: boolean;

    /**
     * Delay to show the tooltip in milliseconds.
     * @group Props
     */
    showDelay?: number | undefined;

    /**
     * Delay to hide the tooltip in milliseconds.
     * @group Props
     */
    hideDelay?: number | undefined;

    /**
     * Time to wait in milliseconds to hide the tooltip even it is active.
     * @group Props
     */
    life?: number | undefined;

    /**
     * Specifies the additional vertical offset of the tooltip from its default position.
     * @group Props
     */
    positionTop?: number | undefined;

    /**
     * Specifies the additional horizontal offset of the tooltip from its default position.
     * @group Props
     */
    positionLeft?: number | undefined;

    /**
     * Whether to hide tooltip when hovering over tooltip content.
     * @group Props
     */
    autoHide?: boolean;

    /**
     * Automatically adjusts the element position when there is not enough space on the selected position.
     * @group Props
     */
    fitContent?: boolean;

    /**
     * Whether to hide tooltip on escape key press.
     * @group Props
     */
    hideOnEscape?: boolean;

    /**
     * Content of the tooltip.
     * @group Props
     */
    content?: string | TemplateRef<HTMLElement> | undefined;

    /**
     * When present, it specifies that the component should be disabled.
     * @defaultValue false
     * @group Props
     */
    tooltipDisabled?: boolean;


}