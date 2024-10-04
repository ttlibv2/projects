import { Component, Input, OnInit, ViewChild } from "@angular/core";
import { DropdownModule } from "primeng/dropdown";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { Template } from "../../models/template";
import { InputTextModule } from "primeng/inputtext";
import { ButtonModule } from "primeng/button";
import { FieldsetModule } from "primeng/fieldset";
import { InputNumberModule } from "primeng/inputnumber";
import { DividerModule } from "primeng/divider";
import {FormArray, FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import { Forms, Objects , Files } from "ts-ui/helper";
import { FileUpload, FileUploadModule } from "primeng/fileupload";
import { ToastService } from "ts-ui/toast";
import { OverlayOptions } from "primeng/api";
import {Workbook} from "ts-ui/exceljs";
import {NgForOf} from "@angular/common";
import {Observable, Observer} from "rxjs";
import { ModalService } from "ts-ui/modal";
const { notNull, isNull, isBlank, notBlank } = Objects;

export interface UrlFile {
    label: string;
    link: string;
    name: string;
    accept?: string;
    selected?: boolean;
    sheets: string[];
    downloadCmd?: any;
}

export interface DefaultData {
    label: string;
    selected?: boolean;
    item: any;
}

export interface InputData {
    files: UrlFile[];
    defaults: DefaultData[];
}

@Component({
    standalone: true,
    selector: 'ts-xsl-template',
    templateUrl: './xsl-template.html',
    imports: [
        ReactiveFormsModule, DropdownModule,
        InputTextModule, ButtonModule, FieldsetModule,
        InputNumberModule, DividerModule, FileUploadModule, NgForOf
    ]
})
export class XslTemplate implements OnInit {
    @Input() defaults: DefaultData[] = [];
    @Input() files: UrlFile[] = [ ];

    @ViewChild(FileUpload, { static: true })
    fileUpload: FileUpload;

    overlayOptions: OverlayOptions = {mode: 'modal'};

    forms: Forms;
    fileXsl: File;
    urlFile: UrlFile;

    asyncLoading: boolean = false;
    workbook: Workbook;
    sheetNames: string[] = [];

    constructor(private fb: FormBuilder,
        private modal: ModalService,
        private dialogRef: DynamicDialogRef,
        private toast: ToastService) {
    }

    get formData(): any {return this.forms?.formRawValue;}
    get defaultData(): DefaultData {return this.formData?.defaultData;}
    get sheets(): FormArray {return this.forms.getControl('sheets') as FormArray;}
    get disabledUrlFile(): boolean { return (this.files?.length || 0) <= 1; }
    get chooseLabel(): string { return isNull(this.fileXsl) ? 'Chọn tệp đính kèm': `Đã chọn ${this.fileXsl.name}`;}

    ngOnInit(): void {
        let instanceData: Partial<InputData> = this.modal.getData(this.dialogRef) ?? {};
        let currentDefault = null;

        if (instanceData) {
            this.files = instanceData.files || [];
            this.files.forEach(f => {
                f.accept = f.accept ?? Files.extractAccept(f.name, null);
               // f.downloadCmd = f.downloadCmd ?? (() => void);
            });

            this.urlFile = this.files.find(f => f.selected) ?? this.files[0];
            this.defaults = instanceData.defaults || [];

            currentDefault = this.defaults.find(d => d.selected);
        }

        const disabledUrlFile = this.files?.length <= 1;
        this.forms = Forms.builder(this.fb, {
            urlFile: [{value: this.urlFile, disabled: disabledUrlFile}, Validators.required],
            defaultData: [currentDefault],
            sheets: this.fb.array([])
        });

        this.changeUrlFile(this.urlFile);
    }

    changeDefaultData(data: DefaultData) {
    }

    changeUrlFile(data: UrlFile): void {
        this.urlFile = data;
        this.sheets.controls = [];
        for(let sheet of (data.sheets || ['data'])) {
            this.addSheetControl(sheet);
        }
    }

    changeSheet(index: number, sheet: string): void {
        if (isBlank(sheet) || isNull(this.workbook)) {
           this.sheets.controls[index].patchValue({
               sheetName: undefined,
               fieldIndex: 2,
               beginRow: 2,
               endRow: 99
           })
        }
    }

    onDownload(): void {}

    async chooseFile(files: File[]) {
        this.fileUpload.clear();
        this.fileXsl = files[0];

        this.workbook = await Workbook.open(this.fileXsl);
        this.sheetNames = this.workbook.getSheetNames();

        if(this.urlFile.sheets?.length > 0) {
            const names: any[] = this.sheets.getRawValue();
            const hasError = names.some(item => !this.sheetNames.includes(item.sheetName));
            if(hasError) {
                this.toast.error('Tệp đính kèm đã chọn không đúng cấu trúc. Vui lòng tải mới ', 'Thông báo !!');
                return;
            }
        }

        for(const ctrl of this.sheets.controls) {
            const sheetName: string = ctrl.get('sheetName').getRawValue();
            const sheet = this.workbook.getSheetByName(sheetName);
            const beginRow = (sheet.frozenRows ?? 2) + 1;
            ctrl.patchValue({beginRow, endRow: sheet.lastRow});
        }

    }

    closeDialog(): void {
        this.dialogRef.destroy();
    }

    handleSubmit(): void {

        const result$ = new Observable((observer: Observer<any>) => {

            const result: any = {
                defaultData: this.forms.getValue('defaultData'),
                workbook: this.workbook,
                fileXsl: this.fileXsl,
                sheets: {}
            };

            for(const ctrl of this.sheets.controls) {
                const {sheetName, fieldIndex, beginRow, endRow} = ctrl.getRawValue();

                const sheet = this.workbook.getSheetByName(sheetName);
                const dataSheet: any = {sheetName, fieldIndex, beginRow, endRow};

                dataSheet.rows = sheet.getJsonRow(fieldIndex, beginRow, endRow);
                result.sheets[sheetName] = dataSheet;
            }

            observer.next(result);
            observer.complete();

        });

        result$.subscribe(data => this.dialogRef.close(data));
    }

    private addSheetControl(sheetName?: string) {
        this.sheets.push(this.fb.group({
            sheetName: [{value: sheetName, disabled: notBlank(sheetName)}, Validators.required],
            fieldIndex: [2, Validators.required],
            beginRow: [3, Validators.required],
            endRow: [3, Validators.required],
        }));

    }
}