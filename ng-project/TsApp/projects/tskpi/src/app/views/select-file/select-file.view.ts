import { Component, OnInit, ViewChild, ViewEncapsulation } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { CellValue, Workbook, Worksheet, WorksheetView, WorksheetViewFrozen } from "exceljs";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { FileSelectEvent, FileUpload } from "primeng/fileupload";
import { delay, Observable, Observer } from "rxjs";
import { Base64, Files, Objects } from "ts-ui/helper";
import { ModalService } from "ts-ui/modal";
import { ToastService } from "ts-ui/toast";
import { KpiConfig } from "../../service/kpi-config";

interface FormVal {
    sheetName: string;
    beginRow: number;
    endRow: number;
}

@Component({
    selector: 'ts-select-file',
    templateUrl: './select-file.view.html',
    styleUrl: './select-file.view.scss',
    encapsulation: ViewEncapsulation.None
})
export class SelectFileView implements OnInit {
   // fileMau: string = '/assets/reportkpi_mau.xlsx';
    fileText: string = 'Chọn file dữ liệu excel xlsx hoặc xls.';
    mimeType: string = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';

    formGroup: FormGroup;
    file: File = undefined;
    isLoading: boolean = false;

    fileBase64: string;
    workbook: Workbook = undefined;
    sheets: string[] = [];
    minRow: number = 3;
    maxRow: number = null;


    get lblFile(): string {
        if(Objects.isNull(this.file)) return this.fileText;
        else return `Bạn đang chọn tệp: ${this.file?.name}`;
    }


    get disabledAction(): boolean {
        return Objects.isNull(this.workbook) || this.isLoading;
    }

    get fileMau(): string {
        return this.cfg.excel_file;
    }

    @ViewChild('fileUpload', { static: true })
    private fileUpload: FileUpload;


    constructor(
        private cfg: KpiConfig,
        private fb: FormBuilder,
        private dialogRef: DynamicDialogRef,
        private toast: ToastService,
        private modal: ModalService) { }

    ngOnInit(): void {
        this.formGroup = this.fb.group({
            sheetName: [undefined, Validators.required],
            beginRow: [{value:1}, Validators.required],
            endRow: [Validators.required],
        })
    }


    async selectXsl(event: FileSelectEvent) {
        this.file = event.files[0];
        this.fileUpload?.clear();
        await this.readXsl(this.file);
    }

    private frozenRow(views: Partial<WorksheetView>[]) {
        const view: WorksheetViewFrozen = <any>views.find(v => v.state === 'frozen');
        return Objects.isNull(view) ? -1 : view.ySplit;
    }


    selectSheet(sheetName: string) {
        const ws:Worksheet = this.workbook.getWorksheet(sheetName);
        const frozenRows = this.frozenRow(ws.views);
        if(frozenRows == -1) {
            this.toast.warning(`Sheet [${sheetName}] không đúng cấu trúc`);
            this.minRow = 0; this.maxRow = 0;
            this.formGroup.setValue({ sheetName: null,  beginRow: 0,  endRow: 0 });
        }
        else{
            this.minRow = frozenRows  + 1; this.maxRow = ws.actualRowCount;
            this.formGroup.setValue({ sheetName: this.sheets[0],  beginRow: this.minRow,  endRow: this.maxRow });
        }
    }

   
    onLoadExcel(): void {
        if(this.formGroup.invalid) {
            this.toast.warning(`Vui lòng nhập đầy đủ thông tin`);
            return;
        }

        this.isLoading = true;

        const {sheetName, beginRow, endRow} = this.formGroup.getRawValue();
        if(beginRow <=0 || endRow <=0) {
           this.toast.error(`Sheet [${sheetName}] đang chọn không đúng cấu trúc.`);
            return;
        }

        const obs = new Observable((observer: Observer<any>) => {
            const ws = this.workbook.getWorksheet(sheetName);
    
            const rows: CellValue[][] = <any>ws.getRows(1, endRow).map(row => row.values);
            const data = rows.map(row => row.splice(1, row.length-1).map(cell => cell.valueOf()));
    
            const rowId: string[] = <any>data[0];
            const rowLabel: string[] = <any>data[1];
    
            const gridColumn: any[] = rowId.map((id, i) => ({field: id, resizable: true, headerName: rowLabel[i]}));
            const gridData = data.splice(beginRow-1, endRow-beginRow+1).map(row => Objects.arrayToJson(rowId, (id, i) => [id, row[i]]));
            
            const indexCol = gridColumn.find(col => col.field === 'index');
            if(indexCol) {
                indexCol['width'] = 80;
                //indexCol['resizable'] = false;
            }

            observer.next({
                grid: {columns: gridColumn, data: gridData},
                file: {
                    name: this.file.name, 
                    mime: this.mimeType, 
                    beginRow, endRow, sheet: sheetName,
                    base64: this.fileBase64
                }
            });

        });

       obs.pipe(delay(1000)).subscribe({
        next: data => {
            this.isLoading = false;
            this.dialogRef.close(data);
        },
        error: msg => {
            this.isLoading = false;
            console.error(msg)
        }

       })
        

    }

    closeDialog(): void {
        this.dialogRef.close();
    }

    private async readXsl(file: File) {
        this.workbook = await file.arrayBuffer().then(buffer => new Workbook().xlsx.load(buffer));
        this.sheets = this.workbook?.worksheets?.map(ws => ws.name) || [];
        this.selectSheet('data');
        this.fileBase64 = await Base64.encode(this.file);
    }
}