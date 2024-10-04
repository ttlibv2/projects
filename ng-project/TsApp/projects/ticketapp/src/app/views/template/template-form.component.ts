import { ChangeDetectorRef, Component, Input, OnInit, signal, Type, ViewChild, ViewEncapsulation, } from "@angular/core";
import { Validators, } from "@angular/forms";
import { TemplateService } from "../../services/template.service";
import { ToastService } from "ts-ui/toast";
import { LoggerService } from "ts-ui/logger";
import { Objects } from "ts-ui/helper";
import { StorageService } from "../../services/storage.service";
import { JsonObject, Severity } from "../../models/common";
import { AgTable, TableColumn, TableOption } from "ts-ui/ag-table";
import { AgCellColor } from "./renderer";
import { GetRowIdParams } from "@ag-grid-community/core";
import { TicketFormComponent } from "../ticket-form/ticket-form.component";
import { Router } from "@angular/router";
import { Template, TemplateThread, TicketTemplate } from "../../models/template";
import { Alert } from "ts-ui/alert";
import { RxjsUtil } from "../ticket-list/rxjs-util";
import { FormBuilder } from "@angular/forms";
import { FormGroup } from "@angular/forms";
import { ModalService } from "ts-ui/modal";
import { EmailTemplateView } from "../email-template/email-template.view";

const { isNull, notBlank, isBlank } = Objects;

interface TemplateInput {
    entity?: string;
    templates?: Template[];
    data?: JsonObject;
}

interface State {
    hasInputTemplates?: boolean; // true if input ?
    hasChange?: boolean;
    asyncTemplate?: boolean;// = false;
    asyncSave?: boolean;// = false;
    visibleDelete?: boolean;
    visibleCopy?: boolean;
}


//Partial<Template>
const defaultInfo: any = {
    text_color: '#475569',
    bg_color: '#f1f5f9'
};

@Component({
    selector: "ts-template-form",
    templateUrl: "./template.component.html",
    styleUrl: "./template-form.component.scss",
    encapsulation: ViewEncapsulation.None,
})
export class TemplateFormComponent implements OnInit {

    get template(): Partial<TicketTemplate> {
        const rawJson = this.formGroup?.getRawValue();
        rawJson.data = JSON.parse(rawJson.data || '{}');
        return rawJson;
    }

    get btnPreviewStyle(): any {
        const { bg_color, text_color, severity } = this.template;
        if (notBlank(severity)) return {};
        else return {
            'background-color': bg_color,
            'color': text_color,
            'border-color': bg_color
        }
    }

    get btnPreviewClass(): any {
        const { severity } = this.template;
        return {
            [`p-button-${severity}`]: notBlank(severity)
        }
    }


    formGroup: FormGroup;

    state: State = {};
    labelSave: string = "save";
    rows1: Template[] = [];
    lsSeverity: Severity[] = ['primary', 'secondary', 'success', 'info', 'danger', 'help', 'warning', 'contrast'];
    lsColor: any[] = [{ label: '', color: '' }];
    lsEntity: string[] = ['ticket_template', 'email_template'];

    columns: TableColumn[] = [
        { field: 'title', headerName: 'Tiêu đề' ,rowDrag: false, headerCheckboxSelection: true, checkboxSelection: true},
        { field: 'summary', headerName: 'Diễn giải' },
        { field: 'icon', headerName: 'Icon', width: 150 },
        { field: 'thread', headerName: 'Mã Form', width: 131  },      
        { field: 'bg_color', headerName: 'Màu nền', width: 87, cellRenderer: AgCellColor },
        { field: 'text_color', headerName: 'Màu chữ', width: 87, cellRenderer: AgCellColor },
        { field: 'data', headerName: 'Dữ liệu', width: 117, cellRenderer: (data: any) => data.value ? 'Có dữ liệu' : '' },
       
    ];

    agOption: TableOption = {
        defaultColDef: { suppressHeaderMenuButton: true, },
        rowSelection: 'multiple',
        onRowSelected: () => this.state.visibleDelete = this.agTable.getSelectedRows().length > 0,
        getRowId: (params: GetRowIdParams<Template>) => `ROWID_${params.data?.template_id}`,
    };

    @ViewChild(AgTable)
    agTable: AgTable;

    @Input({ alias: 'thread' })
    thread: string;

    @Input()
    lastUrl: string;

    constructor(
        private fb: FormBuilder,
        private toast: ToastService,
        private alert: Alert,
        private config: StorageService,
        private logger: LoggerService,
        private router: Router,
        private def: ChangeDetectorRef,
        private modal: ModalService,
        private templateSrv: TemplateService) {
        // this.createFormGroup();
    }

    private createFormGroup() {
        const threadValue = { value: this.thread, disabled: notBlank(this.thread) };

        this.formGroup = this.fb.group({
            icon: [null], template_id: [null],
            thread: [threadValue, Validators.required],
            title: [null, Validators.required],
            summary: [null], bg_color: [null],
            text_color: [null], style: [null],
            data: [{ value: null, disabled: true }, Validators.required],
            severity: [null], edit_data: [true], is_default: [false],
            position: [null]
        });

        this.pathValue({ ...defaultInfo });



    }

    ngOnInit() {
        this.createFormGroup();
        this.pathValue({ thread: this.thread });
        this.loadTemplate();
    }

    createTemplate() {
        this.resetForm();
        this.state.visibleCopy = false;
        this.state.visibleDelete = false;
        this.tryDisabledThreadControl();
    }

    copyTemplate(): void {
        this.state.visibleDelete = false;
        this.resetForm({
            ...this.template,
            data: JSON.stringify(this.template.data),
            template_id: undefined,
            title: undefined
        });
    }

    loadTemplate() {
        this.state.asyncTemplate = true;
        this.state.visibleDelete = false;

        const loading = this.toast.loading(`Đang lấy dữ liệu ${this.thread ?? ''}.`);

        const entities = notBlank(this.thread) ? [this.thread] : [];
        this.templateSrv.getAllByUser(...entities).subscribe({
            next: (page) => {
               // this.rows = page.data;
                this.state.asyncTemplate = false;
                this.agTable.setRows(...page.data);
                this.toast.success(this.config.i18n.loadTemplateOk);
                this.toast.close(loading);
            },
            error: (err) => {
                this.state.asyncTemplate = false;
                this.logger.error(`loadTemplate error: `, err);
                this.toast.close(loading);
            },
        });
    }

    /** Save template to server */
    saveTemplate() {

        if (this.formGroup.invalid) {
            this.toast.warning(this.config.i18n.form_invalid);
            return;
        }
        else {
            this.state.asyncSave = true;

            const formValue = this.formGroup.getRawValue();
            formValue.data = JSON.parse(formValue.data ?? '{}');

            const isNew = isNull(formValue.template_id);

            this.templateSrv.create(formValue).subscribe({
                next: (template: Template) => {
                    this.state.asyncSave = false;
                    if (isNew) this.agTable.addRow(template);
                    else this.agTable.updateRows(template);
                    this.toast.success(this.config.i18n.saveTemplateOk);
                    this.pathValue(template);
                    this.def.detectChanges();
                },
                error: (err) => {
                    this.state.asyncSave = false;
                    this.logger.error(`saveTemplate error: `, err);
                    this.def.detectChanges();
                },
            });


        }


    }

    /** Delete template has selected */
    deleteTemplate() {
        const rows: Template[] = this.agTable.getSelectedRows();
        if (rows.length > 0) {

            const ref = this.alert.warning({
                title: 'Thông báo !!',
                summary: 'Bạn có muốn xóa dòng này hay không ?',
                actions: [
                    { label: 'Đồng ý xóa', severity: 'danger', onClick: e => e.dynamicRef.close('delete') },
                    { label: 'Không xóa', severity: 'primary', onClick: e => e.dynamicRef.close('') }
                ]
            });

            ref.onClose.subscribe(action => {
                if ("delete" === action) {
                    const waitToast = this.toast.help(`Đang xóa dữ liệu. Vui lòng đợi....`);
                    const lsObs = rows.map(row => this.templateSrv.deleteById(row.template_id));
                    RxjsUtil.runConcatMap(lsObs, 1000, true, {
                        error: msg => {
                          this.toast.close(waitToast);
                          this.logger.error(msg);
                        },
                        next: data => {
                          this.logger.info(data);
                          this.agTable.removeRows({ template_id: data['model_id'] })
                        },
                        complete: () => {
                          this.toast.close(waitToast);
                          this.logger.info('complete');
                        }
                      });
                }
            });


        }


    }

    selectTemplate(template: Template): void {
        this.pathValue(template);
        this.state.visibleCopy = true;
        this.tryDisabledThreadControl(true);
        this.formGroup.get('data').disable();

        if (!this.lsEntity.includes(template.thread)) {
            this.toast.warning(`Mã mẫu ${template.thread} không tồn tại.`)
        }
    }

    resetForm(data?: any): void {
        this.formGroup.reset({ ...defaultInfo, ...data });
    }

    settingData() {
        const data = this.template;
        const thread: TemplateThread = <any>data.thread;

        if (isBlank(thread)) {
            this.toast.warning('Vui lòng chọn <b>[Mã mẫu]</b>');
            return;
        }

        const clsType: Type<any> = thread === 'ticket_template' ? TicketFormComponent
            : thread === 'email_template' ? EmailTemplateView: null;

        if(Objects.isNull(clsType)) {
            this.alert.warning({
                title: 'Thông báo !!', 
                summary: `Ứng dụng chưa hỗ tạo thiết lập dữ liệu mẫu [${thread}]`
            });
        }
        else {
            const openRef = this.modal.open(clsType, {
                header: `Cấu hình dữ liệu mặc định -- [${thread}]`,
                closable: true, draggable: false, resizable: false,maximizable: true,
                data: { template: data }
            });
    
            openRef.onClose.subscribe({
                next: res => {
                    if (res && res.data) {
                        this.pathValue({ data: res.data });
                    }
                }
            });
        }





     

    }

    selectSeverity(severity: string): void {

    }

    onChangeRowPosition(event: any): void {
       // console.log(this.rows)
    }

    previousPage(): void {
        this.router.navigate([this.lastUrl]);
    }

    private pathValue(value: Partial<Template>) {
        const data = value.data ? JSON.stringify(value.data, null, 0) : undefined;
        this.formGroup.patchValue({ ...value, data });
    }


    private tryDisabledThreadControl(disabled?: boolean) {
        const func = disabled === true ? 'disable'
            : notBlank(this.thread) ? 'disable' : 'enable';

        this.formGroup.get('thread')[func]();
    }

}