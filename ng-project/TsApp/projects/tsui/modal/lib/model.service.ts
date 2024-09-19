import {Injectable, Type} from "@angular/core";
import {DialogService, DynamicDialogComponent, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";

const defaultConfig: DynamicDialogConfig = {
    position: 'top',
    closable: true,
    closeOnEscape: false,
    focusOnShow: false,
    maximizable: false,
    draggable: false,
    resizable: false,
    showHeader: true,
    dismissableMask: false,
    modal: true,
}

@Injectable({providedIn: 'root' })
export class ModalService {

    constructor(private dialog: DialogService) {
    }

    open<E, D=any>(componentType: Type<E>, config: DynamicDialogConfig<D>): DynamicDialogRef<E> {
        return this.dialog.open(componentType, { ...defaultConfig, ...config });
    }

    /**
     * Returns the dynamic dialog component instance.
     * @param ref DynamicDialog instance.
     * @group Method
     */
    getInstance(ref: DynamicDialogRef): DynamicDialogComponent {
       return this.dialog.dialogComponentRefMap.get(ref)?.instance;
    }

    getData<E>(ref: DynamicDialogRef):E {
        return this.getInstance(ref)?.data;
    }





}