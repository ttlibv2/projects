import { Component, Input, OnInit, ViewChild } from "@angular/core";
import { DropdownModule } from "primeng/dropdown";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { Template } from "../../models/template";
import { InputTextModule } from "primeng/inputtext";
import { ButtonModule } from "primeng/button";
import { FieldsetModule } from "primeng/fieldset";
import { InputNumberModule } from "primeng/inputnumber";
import { DividerModule } from "primeng/divider";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { Forms, Objects , FileUtil } from "ts-ui/helper";
import { FileUpload, FileUploadModule } from "primeng/fileupload";
import { Row, Workbook, Worksheet } from "exceljs";
import { ToastService } from "ts-ui/toast";
import { OverlayOptions } from "primeng/api";
import { Observable, Observer } from "rxjs";
import { ModalService } from "../../services/ui/model.service";
const { notNull, isNull, isBlank } = Objects;


function extractAccept(file: UrlFile) {
    return FileUtil.extractAccept(file.link, file.accept);
}

export interface UrlFile {
    label: string;
    link: string;
    name: string;
    accept?: string;
    select?: boolean;
}

export interface DefaultData {
    label: string;
    select: boolean;
    item: any;
}

export interface InputData {
    urlFiles: UrlFile[];
    defaults: DefaultData[];
}

interface FormData {
    urlFile: UrlFile;
    default: DefaultData;
    sheetName: string;
    fieldIndex: number;
    beginRow: number;
    endRow: number;
    selectFile: File;
    workbook: Workbook;
}





@Component({
    standalone: true,
    selector: 'ts-xsl-template',
    templateUrl: './xsl-template.html',
    imports: [
        ReactiveFormsModule, DropdownModule, 
        InputTextModule, ButtonModule, FieldsetModule, 
        InputNumberModule, DividerModule, FileUploadModule
    ]
})
export class XslTemplate implements OnInit {
    @Input() defaults: DefaultData[] = [];
    @Input() files: UrlFile[] = [ ];

    @ViewChild(FileUpload, { static: true })
    fileUpload: FileUpload;

    forms: Forms<FormData>;

    get formData(): Partial<FormData> {
        return this.forms?.formRawValue;
    }

    get urlFile(): UrlFile {
        return this.formData?.urlFile;
    }

    get defaultData(): DefaultData {
        return this.formData?.default;
    }

    get selectFile(): File {
        return this.formData?.selectFile;
    }

    constructor(private fb: FormBuilder,
        private modal: ModalService,
        private dialogRef: DynamicDialogRef,
        private toast: ToastService) {

    }

    ngOnInit(): void {
        let instanceData: Partial<InputData> = this.modal.getData(this.dialogRef) ?? {};
        let currentFile = null, currentDefault = null;
        

        if(instanceData) {
            this.files = instanceData.urlFiles || [];
            this.files.forEach(f => FileUtil.extractAccept(f.name, f.accept));
            this.defaults = instanceData.defaults || [];

            currentFile = this.files.find(f => f.select);
            currentDefault = this.defaults.find(d => d.select);
        }

        this.forms = Forms.builder<FormData>(this.fb, {
            urlFile: [currentFile, Validators.required],
            default: [currentDefault],
            sheetName: [{value: null, disabled: true}, Validators.required],
            fieldIndex: [2, Validators.required],
            beginRow: [{value: 3, disabled: true}, Validators.required],
            endRow: [{value: 3, disabled: true}, Validators.required],
            selectFile: [null]
        })
    }


    downloadFileMau(): void { }

    changeTemplate(data: Template) { 
        //this.currentTemplate = data;
        //this.resetWorkBook();
    }

    changeFileXsl(data: UrlFile): void {
        //this.currentFile = data;
    }

    changeSheet(sheet: string): void {
        // if (isBlank(sheet) || isNull(this.workbook)) {
        //     this.maxRow = 99;
        //     this.worksheet = null;
        //     this.fieldIndex = 2;
        //     this.forms.pathValue({ beginRow: 3, endRow: 99, fieldIndex: 2 })
        // }
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
        // this.fileUpload.clear();
        // this.file = files[0];
        // this.worksheet = null;
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


    private getCellValue(row: Row): any[] {
        return Objects.fillArray(row.actualCellCount).map(c => row.getCell(c + 1).text);
    }

    private valuesToJson(fields: string[], values: string[]) {
        return Objects.arrayToJson(fields, (field, index) => [field, values[index]]);
    }

    overlayOptions: OverlayOptions = {
        mode: 'modal'
    };
}