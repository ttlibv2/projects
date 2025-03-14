import {
    AfterContentInit,
    Component,
    ContentChildren,
    Input,
    OnDestroy,
    OnInit,
    QueryList,
    TemplateRef,
    ViewChild,
    ViewEncapsulation
} from '@angular/core';
import { AgTable, ExportXslOption, TableColumn, TableOption } from "ts-ui/agtable";
import { FormGroup, FormsBuilder } from "ts-ui/forms";
import { ToastService } from "ts-ui/toast";
import { ModalService } from "ts-ui/modal";
import { Asserts, Callback, Objects, Page, Supplier } from "ts-ui/helper";
import { PrimeTemplate } from "primeng/api";
import { delay, map, Observable } from 'rxjs';
import { StorageService } from '../../../services/storage.service';
import { Alert } from 'ts-ui/alert';
import { RxjsUtil } from '../rxjs-util';
import { AbstractColDef, ColDef, ColDefField, ColGroupDef, ColumnGroup } from '@ag-grid-community/core';

const { notBlank, isNull, notNull, isFalse, notEmpty } = Objects;

export interface Field {
    fieldId: string;
    label: string;
    type: string;
    class?: string;
    view?: boolean;
    placeholder?: string;
    options?: any;
    tb_width?: number;
    tb_option?: TableColumn;
    fieldType?: string | 'group' | 'control' | 'array';
    children?: Field[];
}

interface State {
    lsDs?: boolean;
    save?: boolean;
    delete?: boolean;
}


export interface MvcOption<E = any> {
    loadDataFunc?: Supplier<Observable<Page<E>>>;
    resetDataFunc?: Supplier<E>;
    newDataFunc?: Callback<E, Observable<E>>;
    editDataFunc?: Callback<E, Observable<E>>;
    deleteDataFunc?: Callback<E, Observable<any>>;


    formFields?: Field[];
    visibleImport?: boolean;
    visibleExport?: boolean;
    visibleDeleteAll?: boolean;
    autoLoadData?: boolean;

    //page_width
    pageWidth?: string;
    pageSize?: number;

    // table
    rowNameId?: string;
    tbHeight?: string;
    tbColumns?: TableColumn[];
    tbOption?: TableOption;

    // export
    xsl_options?: Partial<ExportXslOption>;

    // form
    formGroup?: (fb: FormsBuilder) => FormGroup;





}

interface LoadEvent {
    event?: any;
    grid: AgTable;
}

interface SaveEvent<E = any> {
    event?: any;
    grid: AgTable;
    data: Partial<E>;
}



@Component({
    selector: 'ts-mvc',
    templateUrl: './mvc.component.html',
    styleUrl: './mvc.component.scss',
    encapsulation: ViewEncapsulation.None
})
export class MvcComponent<E = any> implements AfterContentInit, OnInit, OnDestroy {
    private _defaultOption: MvcOption = {
        tbHeight: '450px',
        tbOption: {
            domLayout: 'normal',
            rowSelection: 'multiple',
            getRowId: e => `RID_${e.data['id']}`,
            onRowSelected: e => this.hasRowSelected = this.agTable.getSelectedNodes().length > 0,
            pagination: true,
            paginationPageSize: 20,
        },
        xsl_options: {
            fileName: 'file.xlsx',
            sheetName: 'data',
            includeColId: true
        }
    };

    private _mvcOption: MvcOption<E> = { ...this._defaultOption };


    //-------------------------------

    @Input()
    set options(opt: MvcOption<E>) {
        Asserts.notEmpty(opt.formFields, "@MvcOption.formFields");

        if (isNull(opt.tbColumns)) {

            opt.tbColumns = [
                {
                    field: opt.rowNameId || 'id',
                    headerName: 'U_ID',
                    width: 100, headerCheckboxSelection: true,
                    checkboxSelection: true
                }
            ];

            opt.tbColumns.push(...opt.formFields.map(f =>this.formFieldToColumn(f)));

        }

        if (notBlank(opt.rowNameId) && isNull(opt.tbOption?.getRowId)) {
            opt.tbOption = opt.tbOption || {};
            opt.tbOption.getRowId = e => `RID_${e.data[opt.rowNameId]}`;
        }

        if(notNull(opt.formGroup)) {
            this.formGroup = opt.formGroup(this.fb);
        }
        else {

            this.formGroup = this.fb.group({});
            for(const field of opt.formFields) {
                let c = this.fb.control({value: null});
                this.formGroup.addControl(field.fieldId, c);
            }
        }

        this._mvcOption = Objects.mergeDeep({}, this._defaultOption, opt);
    }

    @Input() formGroup: FormGroup;
    @Input() submitLabel: string = 'actions.save';


    @ContentChildren(PrimeTemplate)
    templates: QueryList<PrimeTemplate>;

    @ViewChild('agTable', { static: true })
    private agTable: AgTable;

    get options(): MvcOption<E> {
        return this._mvcOption;
    }

    get formFields(): Field[] {
        return this.options?.formFields || [];
    }

    get visibleDel(): boolean {
        return this.options?.visibleDeleteAll === true ? this.hasRowSelected : false;
    }


    formAction: TemplateRef<any> | null;
    hasRowSelected: boolean = false;
    state: State = {};
    allData: E[] = [];

    constructor(private fb: FormsBuilder,
        private toast: ToastService,
        private config: StorageService,
        private modal: ModalService,
        private alert: Alert) {
    }

    private formFieldToColumn(f: Field): any {
        const column: AbstractColDef = {...f.tb_option};
        column.headerName = column.headerName || f.label || f.fieldId;

        if(notEmpty(f.children)) {
            let col:ColGroupDef = <any>column;
            col.groupId = f.fieldId;
            col.children = f.children.map(c => this.formFieldToColumn(c));
        }
        else {
            let col:ColDef = <any>column;
            col.field = f.fieldId
        }

        return column;
    }

    ngAfterContentInit() {
        this.templates?.forEach(item => {
            switch (item.getType()) {
                case 'formAction':
                    this.formAction = item.template;
                    break;
            }
        });
    }

    ngOnInit() {

        if (this.options?.autoLoadData) {
            this.clickLoadData();
        }


    }

    ngOnDestroy() {
    }

    clickNew(): void {
        if (this.formGroup.pristine) {
            this.formGroup.reset(this.getDataReset);
        }
        else {
            const ref = this.alert.warning({
                title: 'Cảnh báo !!',
                summary: this.config.i18n.warnNewData,
                actions: [
                    { label: 'Cập nhật lại', onClick: e => e.dynamicRef.close('ok') },
                    { label: 'Bỏ qua', onClick: e => e.dynamicRef.close('cancel') }
                ]
            });

            ref.onClose.subscribe(act => {
                if ("ok" === act) {
                    this.formGroup.reset(this.getDataReset);
                }
            });
        }
    }


    clickLoadData(): void {
        const loadingRef = this.toast.loading(this.config.i18n.awaitHandle);

        this.state.lsDs = true;

        this.loadDataFunc().subscribe({
            error: msg => {
                console.error(`clickLoadData: `, msg);
                this.toast.close(loadingRef);
                this.state.lsDs = false;
                this.toast.error(`Xảy ra lỗi lấy danh sách dữ liệu.`);
            },
            next: page => {
                this.toast.close(loadingRef);
                this.state.lsDs = false;
                this.toast.success(`[${page.total}] Lấy dữ liệu thành công`);
                this.allData = page.data;

                // grid_page
                const pageSize = this.options.tbOption.paginationPageSize;
                this.agTable.pagination = page.total > pageSize;
            }
        })
    }

    clickSave(): void {
        if (this.formGroup.invalid) {
            this.toast.warning(this.config.i18n.form_invalid);
            return;
        }

        let data = this.formGroup.getRawValue();
        let isNew = isNull(data[this.options.rowNameId]);
        let funcSave = isNew ? this.newDataFunc : this.editDataFunc;

        this.state.save = true;
        funcSave(data).pipe(delay(500)).subscribe({
            error: msg => {
                console.error(`clickSave: `, msg);
                this.state.save = false;
                this.toast.error(this.config.i18n.msgSaveError);
            },
            next: data => {
                this.state.save = false;
                this.toast.success(this.config.i18n.msgSaveOk);
                isNew ? this.agTable.addRow(data) : this.agTable.updateRow(data);
            },
        });
    }

    clickDelete(): void {
        const data = this.agTable.getSelectedRows();
        if (data.length > 0) {
            const ref = this.alert.warning({
                title: 'Cảnh báo !!',
                summary: this.config.i18n.warnDeleteAll,
                actions: [
                    { label: 'Tôi muốn xóa', onClick: e => e.dynamicRef.close('ok') },
                    { label: 'Bỏ qua', onClick: e => e.dynamicRef.close('cancel') }
                ]
            });

            ref.onClose.subscribe(act => {
                if ("ok" === act) {
                    const lsObs = data.map(item => this.deleteDataFunc(item)
                        .pipe(map((res: any) => ({ grid: item, ...res }))));

                    RxjsUtil.runConcatMap(lsObs, 500, true, {
                        error: msg => {
                            console.error(`clickDelete: `, msg);
                            this.state.delete = false;
                            this.toast.error(this.config.i18n.msgDeleteError);
                        },
                        next: data => {
                            this.state.delete = false;
                            this.agTable.removeRows(data.grid);
                        }
                    });

                }
            });
        }
    }

    clickRow(row: E): void {
        this.formGroup.reset(row);
    }

    clickImport(): void { 
        // XslTemplate.openModal(this.modal, {
        //     header: 'Nạp dữ liệu từ excel',
        //     files: [ 
        //         {title: 'Dữ liệu mẫu', link: 'link', sheets: []},
        //     ]
        // })
      
    }

    clickExport(): void { 
      

        const allNode = this.agTable.getSelectedNodes();
        if(allNode.length === 0) {
            this.toast.warning(this.config.i18n.noRowExport);
        }
        else {
            let options = this.options.xsl_options;
            this.agTable.exportXsl(options).subscribe({
                next: res => Objects.download(res.fileName, res.blob)
            });

        }
    }


    get loadDataFunc() {
        return Asserts.notNull(this.options?.loadDataFunc, "@loadDataFunc");
    }

    get resetDataFunc() {
        return Asserts.notNull(this.options?.resetDataFunc, "@resetDataFunc");
    }

    get deleteDataFunc() {
        return Asserts.notNull(this.options?.deleteDataFunc, "@deleteDataFunc");
    }

    get newDataFunc() {
        return Asserts.notNull(this.options?.newDataFunc, "@newDataFunc");
    }

    get editDataFunc() {
        return Asserts.notNull(this.options?.editDataFunc, "@editDataFunc");
    }

    private get getDataReset(): any {
        return this.resetDataFunc();
    }
}
