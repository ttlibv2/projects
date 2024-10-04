import { CommonModule } from "@angular/common";
import { Component, Input, OnInit } from "@angular/core";
import { ButtonModule } from "primeng/button";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { ListboxModule } from "primeng/listbox";
import { DividerModule } from "primeng/divider";
import { HttpClient } from "@angular/common/http";
import { ToastService } from "ts-ui/toast";
import { Objects } from "ts-ui/helper";
import { LoggerService } from "ts-ui/logger";
import { ModalService } from "ts-ui/modal";

export interface NameItem {
    label: string;
    name: string;
    path: string;
    icon?: string;
    mime?: string;
}

@Component({
    standalone: true,
    selector: '[ts-template-file]',
    imports: [CommonModule, ListboxModule, ButtonModule, DividerModule],
    template: `
        <div class="grid">
            <div class="col-12 flex flex-column gap-2">
                <span>- Hướng dẫn nhập liệu trong file mẫu.</span>
                <span>- File mẫu là file excel xlsx hoặc xls.</span>
                <span>- Tải file mẫu bên dưới</span>
            </div>
            <div class="col-12">
                <p-divider [layout]="'horizontal'"></p-divider>
                <p-listbox [options]="list" optionLabel="label" 
                    dataKey="path" emptyMessage="Chưa cấu hình danh sách mẫu"
                    (onClick)="saveFile($event.option)">
                    <ng-template pTemplate="item" let-item>
                        <div class="flex gap-2">
                        @if(item.icon){<span [ngClass]="item.icon"></span>}
                        <a (click)="saveFile(item)">{{item.label}}</a>
                        </div>
                    </ng-template>
                </p-listbox>
            </div>
            <div class="col-12">
                <p-button label="Đóng" (onClick)="closeDialog()"></p-button>
            </div>
        </div>
       
    `
})
export class TemplateFile implements OnInit {
    @Input() list: NameItem[] = [];

    constructor(
        private client: HttpClient,
        private logger: LoggerService,
        private toast: ToastService,
        private modal: ModalService,
        private dialogRef: DynamicDialogRef) { }

    ngOnInit(): void {
        const instance = this.modal.getInstance(this.dialogRef);
        if (instance && instance.data) {
            const { files } = instance.data;
            this.list = files || [];
        }
    }

    closeDialog(): void {
        this.dialogRef.destroy();
    }

    saveFile(item: NameItem) {
        const mime = item.mime || 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
        this.client.get(`${item.path}?file=${mime}`, {responseType: 'blob'}).subscribe({
            error: reason => {
                this.toast.error(`Đã xảy ra lỗi tải tệp đính kèm -> ${reason}`);
                this.logger.error(item.label, reason);
            },
            next: blob => {
                console.log(blob);                
                //let blob = new Blob([buffer], { type: mime });
                Objects.download(item.name, blob)
            }
        })
    }

    click(evet: any) {
        console.log(evet);
    }

}