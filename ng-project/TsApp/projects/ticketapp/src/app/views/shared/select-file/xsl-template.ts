import { ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit, ViewChild, ViewEncapsulation } from "@angular/core";
import { DropdownModule } from "primeng/dropdown";
import { DynamicDialogConfig, DynamicDialogRef } from "primeng/dynamicdialog";
import { InputTextModule } from "primeng/inputtext";
import { ButtonModule } from "primeng/button";
import { FieldsetModule } from "primeng/fieldset";
import { InputNumberModule } from "primeng/inputnumber";
import { DividerModule } from "primeng/divider";
import { AbstractControl, FormArray, FormControl, Validators } from "@angular/forms";
import { Objects, Files, JsonAny } from "ts-ui/helper";
import { FileSelectEvent, FileUpload, FileUploadModule } from "primeng/fileupload";
import { ToastService } from "ts-ui/toast";
import { Message, OverlayOptions } from "primeng/api";
import { CommonModule, NgForOf } from "@angular/common";
import { from, Observable, Observer, Subscription } from "rxjs";
import { ModalService } from "ts-ui/modal";
import { FormGroup, FormsBuilder, FormsModule } from "ts-ui/forms";
import { Row, Workbook, WorksheetViewFrozen } from "exceljs";
import { TooltipModule } from "primeng/tooltip";
import { TranslateModule } from "@ngx-translate/core";
import { CheckboxModule } from "primeng/checkbox";
import { InputTextareaModule } from "primeng/inputtextarea";
import { MessagesModule } from "primeng/messages";
const { isNull, isBlank, notNull, notBlank, isFalse, isEmpty } = Objects;

const DEFAULT_SIZE = 5 * 1024 * 1024;

export interface UrlFile {
    title: string;
    name?: string;
    sheets: string[];
    editSheet?: boolean;
    link?: string;
    clickGet?: () => void;
}

export interface XslField {
    fieldId?: string;
    name: string;
    label: string;
    type: 'input' | 'area' | 'combo' | 'checked' | 'number';
    value?: any;
    required?: boolean;
    class?: string;
    placeholder?: string;
    options?: any;
}

interface WsSheetInfo {
    sheetName: string;
    row_id: number;
    first_row: number;
    last_row: number;
    hasError?: boolean;
}

export interface InputOption {
    file: UrlFile;
    fields?: XslField[];
    maxFileSize?: number;
}

export interface ReturnData {
    form_value: {
        [field: string]: any;
        sheets: WsSheetInfo[];
    };

    file_xsl: File;
    workbook: Workbook;
    sheets: {
        [sheetName: string]: {
            column_ids: string[];
            row_idx: number;
            first_row: number;
            last_row: number;
            name: string;
            rows: any[];
        }
    }
}

export function openXslTemplate(modal: ModalService, input: InputOption, dialogCfg?: DynamicDialogConfig) {
    dialogCfg = Objects.mergeDeep({ header: 'Nạp dữ liệu từ file mẫu', width: '630px' }, dialogCfg);
    dialogCfg.data = input;
    return modal.open(XslTemplate, dialogCfg);
}




@Component({
    standalone: true,
    selector: 'ts-xsl-template',
    templateUrl: './xsl-template.html',
    styleUrl: './xsl-template.scss',
    encapsulation: ViewEncapsulation.None,
    imports: [
        CommonModule,
        FormsModule, DropdownModule,
        InputTextModule, ButtonModule, FieldsetModule,
        TooltipModule, TranslateModule, CheckboxModule,
        InputTextareaModule, MessagesModule,
        InputNumberModule, DividerModule, FileUploadModule, NgForOf
    ]
})
export class XslTemplate implements OnInit, OnDestroy {
    readonly mimeType: string = Files.XLSX_TYPE;


    @ViewChild(FileUpload, { static: true })
    private fileUpload: FileUpload;

    overlayOptions: OverlayOptions = { mode: 'modal' };

    form: FormGroup;
    cSheets: FormArray;


    asyncSubmit: boolean = false;
    asyncLoading: boolean = false;


    fileSelect: File;
    fileUrl: UrlFile;

    workbook: Workbook;
    wbSheetNames: string[] = [];
    wsSheets: { [index: number]: WsSheetInfo } = {};
    chooseFileSub: Subscription;
    maxFileSize: number = DEFAULT_SIZE;
    xslFields: XslField[] = [];
    msgs: Message[] = [];

    get formData(): any {
        return this.form?.getRawValue();
    }

    get chooseLabel(): string {
        return isNull(this.fileSelect) ? 'Chọn tệp dữ liệu mẫu (*.xlsx)'
            : `Bạn đã chọn tệp: ${this.fileSelect.name}`;
    }

    get disabledSubmit(): boolean {
        if (this.form.invalid) return true;
        else return isNull(this.workbook);
    }

    constructor(private fb: FormsBuilder,
        private modal: ModalService,
        private dialogRef: DynamicDialogRef,
        private toast: ToastService) {
    }

    fieldId(field: XslField): string {
        return field.fieldId || field.name;
    }

    ngOnDestroy(): void {
        this.dialogRef?.destroy();
    }


    ngOnInit(): void {
        this.cSheets = this.fb.array([]);
        this.form = this.fb.group({ sheets: this.cSheets });

        let isData: InputOption = this.modal.getData(this.dialogRef);
        if (isData && isData.file && isData.file.sheets?.length) {
            this.maxFileSize = isData.maxFileSize || DEFAULT_SIZE;
            this.xslFields = isData.fields || [];
            this.fileUrl = isData.file;

            // add form_array [sheets]
            for (let sheet of this.fileUrl.sheets) {
                this.addSheetControl(sheet);
            }

            // add form_group [fields]
            this.xslFields.forEach(field => {
                const { name, value, required } = field;
                const control = this.fb.control(value);
                if (required == true) control.addValidators(Validators.required);

                control.patchValue(value);
                this.form.addControl(name, control);
            });

        }
    }


    async chooseFile(event: FileSelectEvent) {
        this.fileUpload.clear();
        this.fileSelect = undefined;
        this.msgs = [];

        const file = event.currentFiles[0];
        if (notNull(file)) {
            this.asyncLoading = true;

            const wbPromise = file.arrayBuffer()
                .then((buffer: Buffer) => new Workbook().xlsx.load(buffer));

            this.chooseFileSub = from(wbPromise).subscribe({
                error: msg => {
                    console.error(`chooseFile: `, msg);
                    this.msgs = [{
                        severity: 'error',
                        summary: `Đã xảy ra lỗi đọc dữ liệu`,
                        detail: msg,
                    }];

                    this.asyncLoading = false;
                },
                next: (wb: Workbook) => {
                    this.workbook = wb;
                    this.wbSheetNames = this.workbook.worksheets.map(ws => ws.name);
                    this.asyncLoading = false;

                    if (this.fileUrl.sheets?.length > 0) {
                        const names: string[] = this.fileUrl.sheets;
                        const hasError = names.some(name => !this.wbSheetNames.includes(name));
                        if (hasError) {
                            this.msgs = [{
                                severity: 'warn',
                                summary: `Tệp đính kèm [${file.name}] đã chọn không đúng cấu trúc.`,
                                detail: `Vui lòng tải mới tệp mẫu`
                            }];
                            return;
                        }
                    }


                    this.fileSelect = file;

                    for (let pos = 0; pos < this.cSheets.controls.length; pos++) {
                        this.changeSheet(pos, this.fileUrl.sheets[pos]);
                    }

                }
            })
        }


    }


    changeSheet(index: number, sheetName: string): void {
        if (isNull(this.workbook) || isBlank(sheetName)) {
            this.cSheets.controls[index].patchValue({
                sheetName: undefined,
                row_id: 2,
                first_row: 2,
                last_row: 99
            })
        }

        // update sheet
        else {
            const sheet = this.workbook.getWorksheet(sheetName);
            const view: WorksheetViewFrozen = <any>sheet.views.find(v => v.state === 'frozen');
            const ctrl = this.cSheets.controls.at(index);
            if (isNull(view)) {
                ctrl.reset();
                this.msgs = [{ severity: 'warn', summary: `Sheet [${sheetName}] không đúng cấu trúc` }];
            }
            else {
                const row_id = view.ySplit;
                const first_row = row_id + 1, last_row = sheet.actualRowCount;
                this.wsSheets[index] = { sheetName, first_row, last_row, row_id };
                ctrl.patchValue({ sheetName, first_row, last_row, row_id });
            }

        }
    }

    changeRowNum(i: number, fg: AbstractControl, evt: any) {
        const { first_row, last_row } = fg.getRawValue();
        const hasErr = first_row > last_row;
        this.wsSheets[i].hasError = hasErr;
        if (hasErr) fg.get('first_row').setErrors({});
        else fg.get('first_row').setErrors(null);
    }


    onDownload(): void {
        const { clickGet, link } = this.fileUrl;
        if (notNull(clickGet)) clickGet();
        else if (notBlank(link)) window.open(link);
    }


    closeDialog(): void {
        if (notNull(this.chooseFileSub)) {
            this.chooseFileSub.unsubscribe();
            this.workbook = null;
            this.wsSheets = null;
        }

        this.dialogRef.close();
    }

    handleSubmit(): void {
        if (this.disabledSubmit) {
            this.toast.warning(`Vui lòng nhập đầy đủ thông tin`);
            return;
        }

        this.asyncSubmit = true;
        const obse = new Observable((observer: Observer<any>) => {
            const formData = this.form.getRawValue();

            // get sheet_data
            const sheets: any = {};
            for (let object of formData.sheets) {
                const { sheetName, row_id, first_row, last_row } = object;
                const ws = this.workbook.getWorksheet(sheetName);

                const cid = this.rowToValue(ws.getRow(row_id));
                const rows = ws.getRows(first_row, last_row - first_row + 1)
                    .map(row => this.rowToValue(row, cid))

                sheets[sheetName] = {
                    rows: rows,
                    column_ids: cid,
                    name: sheetName,
                    id_index: row_id ,
                    first_row: first_row,
                    last_row: last_row
                };

            }

            observer.next({
                form_value: formData,
                file_xsl: this.fileSelect,
                workbook: this.workbook,
                sheets: sheets
            });

        });

        obse.subscribe({
            error: msg => {
                console.error(`handleSubmit: `, msg);
                this.toast.warning(`Đã xảy ra lỗi: ${msg}`);
                this.asyncSubmit = false;
            },
            next: data => {
                this.dialogRef.close(data);
                this.asyncSubmit = false;
            }
        })


    }

    private rowToValue(row: Row, cid?: string[]): any {
        if (isEmpty(cid)) return Objects.fillArray(row.actualCellCount).map(i => row.findCell(i + 1).value.valueOf());
        else return Objects.arrayToJson(cid, (id, i) => [id, row.getCell(i + 1).value.valueOf()]);
    }

    private addSheetControl(sheetName?: string) {
        const disabled = isFalse(this.fileUrl.editSheet);
        this.cSheets.push(this.fb.group({
            sheetName: [{ value: sheetName, disabled }, Validators.required],
            row_id: [2, Validators.required],
            first_row: [3, Validators.required],
            last_row: [3, Validators.required],
        }));

    }
}