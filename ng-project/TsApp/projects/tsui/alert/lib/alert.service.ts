import { Injectable } from "@angular/core";
import { DialogService, DynamicDialogRef } from "primeng/dynamicdialog";
import { AlertOption } from "./alert.interface";
import { AlertContent } from "./alert-content";
import { AlertFooter } from "./alert-footer";
import { Objects } from "ts-ui/helper";
const { notNull } = Objects;

const defaultOption: AlertOption = {
    position: 'top',
    okButton: {
        label: 'Đồng ý',
        onClick: evt => {
            console.log('click')
            evt.dynamicRef.close();
        }
    },
    cancelButton: {
        label: 'Hủy bỏ',
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
        const option:AlertOption = Objects.mergeDeep({ position: 'top'} as AlertOption, config);

        const hasButtonOk = config.okButton || config.okClick || config.okLabel;
        const hasButtonCancel = config.cancelButton || config.cancelLabel || config.cancelClick;
        const hasButton: boolean = notNull(hasButtonOk) || notNull(hasButtonCancel) || config.actions?.length > 0;

        if(hasButtonOk || hasButton === false) {
            const btn = option.okButton = Object.assign({...defaultOption.okButton}, option.okButton);
            btn.label = option.okLabel ?? btn.label ?? 'OK';
            btn.onClick = option.okClick ?? btn.onClick;
        }
       
        if(hasButtonCancel) {
            const btn = option.cancelButton = Object.assign({...defaultOption.cancelButton}, option.cancelButton);
            btn.label = option.cancelLabel ?? btn.label ?? 'Cancel';
            btn.onClick = option.cancelClick ?? btn.onClick;
        }

        return this.dialog.open(AlertContent, {
            styleClass: clazz.join(' '),
            closable: false,
            closeOnEscape: false,
            focusOnShow: false,
            header: config?.title,
            position: option.position,
            minY: 150,
            templates: { footer: AlertFooter},
            data: option,
            duplicate: true,
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