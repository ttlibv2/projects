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
import { ModalService } from "../../services/ui/model.service";
import {Workbook} from "ts-ui/exceljs";
import {NgForOf} from "@angular/common";
const { notNull, isNull, isBlank } = Objects;

export interface UrlFile {
    label: string;
    link: string;
    name: string;
    accept?: string;
    selected?: boolean;
    sheets: string[];
}

export interface DefaultData {
    label: string;
    selected?: boolean;
    item: any;
}

export interface InputData {
    url_files: UrlFile[];
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


    get formData(): any {
        return this.forms?.formRawValue;
    }

    get defaultData(): DefaultData {
        return this.formData?.defaultData;
    }

    get sheets(): FormArray {
        return this.forms.getControl('sheets') as FormArray;
    }

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

    ngOnInit(): void {
        let instanceData: Partial<InputData> = this.modal.getData(this.dialogRef) ?? {};
        let  currentDefault = null;

        if (instanceData) {
            this.files = instanceData.url_files || [];
            this.files.forEach(f => Files.extractAccept(f.name, f.accept));
            this.defaults = instanceData.defaults || [];
            this.urlFile = this.files.find(f => f.selected);

            currentDefault = this.defaults.find(d => d.selected);
        }

        this.forms = Forms.builder(this.fb, {
            urlFile: [this.urlFile, Validators.required],
            defaultData: [currentDefault],
            sheets: this.fb.array([])
        });

        this.addSheetControl();

    }



    downloadFileMau(): void { }

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
        // else {
        //     this.worksheet = this.workbook.getWorksheet(sheet);


        //     const { beginRow, endRow } = this.forms.formRawValue;

        //     const view: any = this.worksheet.views?.find(v => v.state === 'frozen');
        //     const fieldIndex = view?.ySplit || 2;//this.worksheet.views?.find(v => v.state === 'frozen')?.ySplit || 2;

        //     const maxRow = this.worksheet.actualRowCount;
        //     const ibeginRow = (isNull(beginRow) || beginRow <= fieldIndex || beginRow > maxRow) ? fieldIndex + 1 : beginRow;
        //     const iendRow = isNull(endRow) ? maxRow : endRow > maxRow ? maxRow
        //         : endRow < ibeginRow ? ibeginRow : endRow;


        //     this.maxRow = maxRow;
        //     this.fieldIndex = fieldIndex;
        //     this.forms.pathValue({ beginRow: ibeginRow, endRow: iendRow, fieldIndex: fieldIndex });
        // }
    }

    async chooseFile(files: File[]) {
        this.fileUpload.clear();
        this.fileXsl = files[0];

        this.fileXsl.arrayBuffer().then(buffer => Workbook.openByBuffer(buffer))
        // this.workbook = await this.file.arrayBuffer().then(buffer => new Workbook().xlsx.load(buffer));
        // this.sheets = this.workbook.worksheets.map(s => s.name);

        // this.forms.pathValue({ sheetName: this.sheets[0] });
        // this.forms.setDisableControl(false, ['sheetName', 'beginRow', 'endRow']);

        // this.changeSheet(this.sheets[0]);
    }

    closeDialog(): void {
        this.dialogRef.destroy();
    }

    handleSubmit(): void {
        // if (isNull(this.worksheet)) {
        //     this.toast.warning('Vui lòng chọn [Sheet]');
        //     return;
        // }

        // const result$ = new Observable((obsever: Observer<any>) => {
        //     const { beginRow, endRow, fieldIndex, template, fileXsl } = this.forms.formRawValue;

        //     if (beginRow > endRow) {
        //         this.forms.setControlError('beginRow', {});
        //         obsever.error({ msg: 'Số dòng bắt đầu > Số dòng kết thúc' });
        //         obsever.complete();
        //     }

        //     const rowId = this.worksheet.getRow(fieldIndex);
        //     const fields = this.getCellValue(rowId);

        //     const rows = this.worksheet
        //         .getRows(beginRow, endRow - beginRow + 1)
        //         .map(row => this.getCellValue(row))
        //         .map(values => this.valuesToJson(fields, values));

        //     obsever.next({fields, rows, template, fileXsl});
        //     obsever.complete();
        // });



        // this.asyncLoading = true;

        // result$.subscribe({
        //     error: obj => {
        //         this.toast.error(obj.msg);
        //         this.asyncLoading = false;
        //     },
        //     next: data => {
        //         this.asyncLoading = false;
        //         this.dialogRef.close(data);
        //     }
        // })

    }



    overlayOptions: OverlayOptions = {
        mode: 'modal'
    };

    private addSheetControl(sheetName?: string) {
        this.sheets.push(this.fb.group({
            sheetName: [sheetName, Validators.required],
            fieldIndex: [2, Validators.required],
            beginRow: [3, Validators.required],
            endRow: [3, Validators.required],
        }));

    }
}