import {DefaultNoComponentGlobalConfig, IndividualConfig, ToastrIconClasses,} from "ngx-toastr";
import {InjectionToken} from "@angular/core";
import {ComponentType, DisableTimoutType} from "ngx-toastr";

export interface ToastConfig extends GlobalConfig {
}

export interface MessageConfig<Payload = any> extends GlobalConfig<Payload> {
    title?: string;
    message?: string;
    detail?: string;
    messageIcon?: string;
    width?: string;

}

export interface ToastIcon extends ToastrIconClasses{
    [key: string]: string;
    help: string;
    loading: string;
    closeIcon: string;
}

export interface GlobalConfig<Payload = any> {
    severity?: ToastSeverity | string;
    position?: ToastPosition;
    iconClasses?: Partial<ToastIcon>;

    disableTimeOut: DisableTimoutType;

    /**
     * toast time to live in milliseconds
     * default: 5000
     */
    timeOut: number;
    /**
     * toast show close button
     * default: false
     */
    closeButton: boolean;
    /**
     * time to close after a user hovers over toast
     * default: 1000
     */
    extendedTimeOut: number;
    /**
     * show toast progress bar
     * default: false
     */
    progressBar: boolean;

    progressMode?: 'determinate' | 'indeterminate';

    /**
     * render html in toast message (possibly unsafe)
     * default: false
     */
    enableHtml: boolean;

    /**
     * css class on toast component
     * default: ts-toast
     */
    toastClass: string;

    /**
     * css class on toast title
     * default: toast-title
     */
    titleClass: string;

    /**
     * css class on toast message
     * default: toast-message
     */
    messageClass: string;
    /**
     * animation easing on toast
     * default: ease-in
     */
    easing: string;
    /**
     * animation ease time on toast
     * default: 300
     */
    easeTime: string | number;
    /**
     * clicking on toast dismisses it
     * default: true
     */
    tapToDismiss: boolean;
    /**
     * Angular toast component to be shown
     * default: Toast
     */
    toastComponent?: ComponentType<any>;
    /**
     * Helps show toast from a websocket or from event outside Angular
     * default: false
     */
    onActivateTick: boolean;
    /**
     * New toast placement
     * default: true
     */
    newestOnTop: boolean;
    /**
     * Payload to pass to the toast component
     */
    payload?: Payload;

    minWidth?: string;

    summaryStyle?: { [kclass: string]: any };
    detailStyle?: { [kclass: string]: any };
}

export type ToastSeverity = 'info' | 'success' | 'help' | 'warning' | 'error' | 'loading';
export type ToastPosition = 'top-left' | 'top-right' | 'top-center' | 'bottom-left' | 'bottom-right' | 'bottom-center' | 'center' | 'top-full-width' | 'bottom-full-width';
export const TOAST_CONFIG = new InjectionToken<ToastConfig>('TOAST_CONFIG');

export const DEFAULT_CONFIG: ToastConfig = {
    ...DefaultNoComponentGlobalConfig,
    enableHtml: true,
    progressMode: 'determinate',
    progressBar: false,
    closeButton: true,
    tapToDismiss: false,
    newestOnTop: false,
    toastClass: 'ts-toast',
    messageClass: 'ts-toast-message',
    titleClass: 'ts-toast-title',
    position: 'top-right',
    minWidth: '25rem',
    iconClasses: {
        error: 'pi pi-bolt',
        info: 'pi pi-info-circle',
        success: 'pi pi-check-circle',
        help: 'pi pi-question-circle',
        loading: 'pi pi-sync pi-spin',
        warning: 'pi pi-exclamation-triangle',
        closeIcon: 'pi pi-times'
    }

}