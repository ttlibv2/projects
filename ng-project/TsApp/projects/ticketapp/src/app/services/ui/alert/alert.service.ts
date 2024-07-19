import { Injectable } from "@angular/core";
import { DialogService, DynamicDialogRef } from "primeng/dynamicdialog";
import { AlertOption } from "./alert.interface";
import { AlertContent } from "./alert-content";
import { AlertFooter } from "./alert-footer";
import { Objects } from "ts-helper";

const defaultOption: AlertOption = {
    position: 'top',
    okButton: {
        label: 'OK',
        onClick: evt => {
            console.log('click')
            evt.dynamicRef.close();
        }
    },
    cancelButton: {
        label: 'Cancel',
        onClick: evt => evt.dynamicRef.close()
    }
};

function anyNotNull(...objects: any[]): boolean {
    return objects.some(o => Objects.notNull(o));
}


@Injectable({ providedIn: 'root' })
export class Alert {

    constructor(private dialog: DialogService) { }

    create(config: AlertOption): DynamicDialogRef<AlertContent> {
        const clazz = [`p-alert`, `p-alert-${config?.severity}`];
        const option:AlertOption = Objects.mergeDeep({...defaultOption}, config);

        if(config.okButton || config.okClick || config.okLabel) {
            const btn = option.okButton = {...option.okButton};
            btn.label = option.okLabel ?? btn.label ?? 'OK';
            btn.onClick = option.okClick ?? btn.onClick;
        }

        if(config.cancelButton || config.cancelLabel || config.cancelClick) {
            const btn = option.cancelButton = {...option.cancelButton};
            btn.label = option.cancelLabel ?? btn.label ?? 'Cancel';
            btn.onClick = option.cancelClick ?? btn.onClick;
        }

        const hasFooter = anyNotNull(option.okButton, option.cancelButton, ...(option.actions ?? []));
        return this.dialog.open(AlertContent, {
            styleClass: clazz.join(' '),
            closable: false,
            closeOnEscape: false,
            focusOnShow: false,
            header: config?.title,
            position: option.position,
            minY: 150,
            templates: { footer: hasFooter ? AlertFooter: undefined},
            data: option
        });

    }

    danger(config: AlertOption) {
        return this.create({
            icon:'pi pi-bolt', 
            ...config,  severity: 'danger'} as AlertOption);
    }

    warning(config: AlertOption) {
        return this.create({
            icon:'pi pi-exclamation-triangle', 
            ...config,  severity: 'warning'} as AlertOption);
    }

    info(config: AlertOption) {
        return this.create({
            icon:'pi pi-info-circle', 
            ...config,  severity: 'warning'} as AlertOption);
    }

}