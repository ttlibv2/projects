import { ButtonProps } from 'primeng/button';
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

export interface AlertButtonOption extends ButtonProps{

    /**
     * Callback to execute when button is clicked.
     * This event is intended to be used with the <p-button> component. Using a regular <button> element, use (click).
     * @param {MouseEvent} event - Mouse event.
     */
    onClick?: (event: AlertButtonEvent) => void;
}