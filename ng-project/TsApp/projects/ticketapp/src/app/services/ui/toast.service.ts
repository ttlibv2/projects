// import { ComponentRef, Injectable, Type } from "@angular/core";
// import { ActiveToast, IndividualConfig, ToastrService } from "ngx-toastr";
// import { DialogService, DynamicDialogComponent, DynamicDialogConfig, DynamicDialogRef } from "primeng/dynamicdialog";
// import { Objects } from "ts-ui/helper";
// import { ToastItem } from "../views/api/toast-item";
//
// const { notBlank, notEmpty } = Objects;
//
// export type ToastSeverity = 'error' | 'warning' | 'info' | 'success' | 'primary' | 'help' | 'loading' | string;
// export type ToastPosition = 'top-left' | 'top-center' | 'top-right' | 'bottom-left' | 'bottom-right' | 'bottom-center';
//
// //export const toastPrefixClass: string = 'p-toast';
//
// export interface ToastConfig extends Partial<IndividualConfig> {
//     closeClass?: string;
//     closeIcon?: string;
//     messageIcon?: string;
//     toastClassPrefix?: string;
//
// }
//
// export interface ToastMessage extends ToastConfig {
//     code?: string;
//     title?: string;
//     summary?: string;
//     details?: any;
//     severity?: ToastSeverity;
//     position?: ToastPosition;
// }
//
// export type CloseRef = ActiveToast<any> | number;
//
// const defaultDialogConfig: DynamicDialogConfig = {
//     position: 'top',
//     closable: false,
//     closeOnEscape: false,
//     focusOnShow: false,
//     maximizable: false,
//     draggable: false,
//     resizable: false,
//     showHeader: true
// }


// @Injectable({ providedIn: 'root' })
// export class ToastService {

    // constructor(
    //     private message: ToastrService,
    //     private dialog: DialogService) {
    // }
    //
    // private prefixWith(...segments: string[]): string {
    //     const config: ToastConfig = this.message.toastrConfig;
    //     return [config.toastClassPrefix, ...segments].map(s => s.toLowerCase()).join('-');
    // }
    //
    // private positionClass(message: ToastMessage): string {
    //     if (notBlank(message.position)) return this.prefixWith(message.position);
    //     else if (notBlank(message.positionClass)) return message.positionClass;
    //     else return this.message.toastrConfig.positionClass;
    // }
    //
    // show(message: ToastMessage, ...closeRef: CloseRef[]): ActiveToast<any> {
    //     if (notEmpty(closeRef)) this.closeToast(...closeRef);
    //
    //     message.position = message.position ?? 'top-right';
    //
    //     const toastType = 'info';//this.prefixWith('message', message.severity);
    //     const positionClass = 'top-right';//this.positionClass(message);
    //
    //     const config: Partial<IndividualConfig<any>> = {
    //         ...message,
    //         toastClass: 'p-toast',//this.prefixWith('message'),
    //         positionClass: positionClass,
    //         toastComponent: ToastItem
    //
    //     };
    //
    //     return this.message.show(message.summary, message.title, config, toastType);
    // }
    //
    // success(message: ToastMessage, ...closeRef: CloseRef[]): ActiveToast<any> {
    //     return this.show({ ...message, severity: 'success' }, ...closeRef);
    // }
    //
    // error(message: ToastMessage, ...closeRef: CloseRef[]): ActiveToast<any> {
    //     return this.show({ ...message, severity: 'error' }, ...closeRef);
    // }
    //
    // info(message: ToastMessage, ...closeRef: CloseRef[]): ActiveToast<any> {
    //     return this.show({ ...message, severity: 'info' }, ...closeRef);
    // }
    //
    // warning(message: ToastMessage, ...closeRef: CloseRef[]): ActiveToast<any> {
    //     return this.show({ ...message, severity: 'warning' }, ...closeRef);
    // }
    //
    // primary(message: ToastMessage, ...closeRef: CloseRef[]): ActiveToast<any> {
    //     return this.show({ ...message, severity: 'primary' }, ...closeRef);
    // }
    //
    // help(message: ToastMessage, ...closeRef: CloseRef[]): ActiveToast<any> {
    //     return this.show({ ...message, severity: 'help' }, ...closeRef);
    // }
    //
    // loading(message: ToastMessage, ...closeRef: CloseRef[]): ActiveToast<any> {
    //     return this.show({
    //         messageIcon: 'pi pi-spin pi-cog',
    //         ...message, severity: 'info', disableTimeOut: true,
    //     }, ...closeRef);
    // }
    //
    // closeToast(...refs: CloseRef[]): void {
    //     for (const ref of refs) {
    //         const toastId = typeof ref === 'object' ? ref.toastId : ref;
    //         this.message.clear(toastId);
    //     }
    // }
    //
    // closeAllDialog(): void {
    //     const dialogRefs = this.dialog.dialogComponentRefMap.keys();
    //     [...dialogRefs].forEach(ref => ref.close());
    // }

    // openDialog<E>(componentType: Type<E>, config: DynamicDialogConfig): DynamicDialogRef<E> {
    //     return this.dialog.open(componentType, { ...defaultDialogConfig, ...config });
    // }

    // /**
    //  * Returns the dynamic dialog component instance.
    //  * @param ref DynamicDialog instance.
    //  * @group Method
    //  */
    // getDialogComponentRef(ref: DynamicDialogRef<any>): ComponentRef<DynamicDialogComponent> {
    //     return this.dialog.dialogComponentRefMap.get(ref);
    // }

// }