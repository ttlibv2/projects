import {Injectable, Type} from "@angular/core";
import {DialogService, DynamicDialogComponent, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";

const defaultConfig: DynamicDialogConfig = {
    position: 'top',
    closable: false,
    closeOnEscape: false,
    focusOnShow: false,
    maximizable: false,
    draggable: false,
    resizable: false,
    showHeader: true
}

@Injectable({providedIn: 'root' })
export class ModalService {

    constructor(private dialog: DialogService) {
    }

    open<E>(componentType: Type<E>, config: DynamicDialogConfig): DynamicDialogRef<E> {
        return this.dialog.open(componentType, { ...defaultConfig, ...config });
        // return null;
    }

    /**
     * Returns the dynamic dialog component instance.
     * @param ref DynamicDialog instance.
     * @group Method
     */
    getInstance(ref: DynamicDialogRef): DynamicDialogComponent {
       return this.dialog.dialogComponentRefMap.get(ref)?.instance;
        // return null;
    }





}