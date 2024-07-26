import { } from 'primeng/button';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';

export type AlertSeverity = 'success' | 'info' | 'warning' | 'danger' | 'help' | 'primary' | 'secondary' | 'contrast';
export type ButtonIconPosition = 'left' | 'right' | 'top' | 'bottom';
export type AlertPosition = 'center' | 'top' | 'bottom' | 'left' | 'right' | 'top-left' | 'top-right' | 'bottom-left' | 'bottom-right';

export interface AlertOption {

    /**
     * Position of the dialog, options are "center", "top", "bottom", "left", "right", "top-left", "top-right", "bottom-left" or "bottom-right".
     * @group Props
     */
    position?: AlertPosition;

    severity?: AlertSeverity;
    title?: string;
    summary?: string;
    id?: string;
    styleClass?: string;
    icon?: string;

    okClick?: (event: AlertButtonEvent) => void;
    okLabel?: string;

    cancelClick?: (event: AlertButtonEvent) => void;
    cancelLabel?: string;

    okButton?: AlertButtonOption;
    cancelButton?: AlertButtonOption;
    actions?: AlertButtonOption[];

}

export interface AlertButtonEvent {
    event: MouseEvent;
    dynamicRef: DynamicDialogRef;
    option: AlertButtonOption;
}

export interface AlertButtonOption {

    /**
     * Text of the button.
     * @group Props
     */
    label: string;

    /**
     * Name of the icon.
     * @group Props
     */
    icon?: string;

    /**
    * Position of the icon.
    * @group Props
    */
    iconPos?: ButtonIconPosition;

    /**
    * Uses to pass attributes to the loading icon's DOM element.
    * @group Props
    */
    loadingIcon?: string;

    /**
     * Whether the button is in loading state.
     * @group Props
     */
    loading?: boolean;

    /**
    * Defines the style of the button.
    * @group Props
    */
    severity?: 'success' | 'info' | 'warning' | 'danger' | 'help' | 'primary' | 'secondary' | 'contrast';

    /**
     * Add a shadow to indicate elevation.
     * @group Props
     */
    raised?: boolean;

    /**
     * Add a circular border radius to the button.
     * @group Props
     */
    rounded?: boolean;

    /**
     * Add a textual class to the button without a background initially.
     * @group Props
     */
    text?: boolean;

    /**
     * Add a border class without a background initially.
     * @group Props
     */
    outlined?: boolean;

    /* 
     * Add a link style to the button.
     * @group Props
     */
    link?: boolean;

    /**
     * Defines the size of the button.
     * @group Props
     */
    size?: 'small' | 'large';

    /**
     * Add a plain textual class to the button without a background initially.
     * @group Props
     */
    plain?: boolean;

    /**
    * When present, it specifies that the component should be disabled.
    * @group Props
    */
    disabled?: boolean;

    /**
    * Add a tabindex to the button.
    * @group Props
    */
    tabindex?: number;

    /**
     * Inline style of the element.
     * @group Props
     */
    style?: { [klass: string]: any };
    /**
     * Class of the element.
     * @group Props
     */
    styleClass?: string;

    /**
     * Used to define a string that autocomplete attribute the current element.
     * @group Props
     */
    ariaLabel?: string;

    /**
     * When present, it specifies that the component should automatically get focus on load.
     * @group Props
     */
    autofocus?: boolean;

    /**
     * Callback to execute when button is clicked.
     * This event is intended to be used with the <p-button> component. Using a regular <button> element, use (click).
     * @param {MouseEvent} event - Mouse event.
     */
    onClick?: (event: AlertButtonEvent) => void;
}