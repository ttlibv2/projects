import { ChangeDetectorRef, Component, Input, OnInit, signal, ViewChild, ViewEncapsulation, } from "@angular/core";
import { FormBuilder, FormGroup, Validators, } from "@angular/forms";
import { TemplateService } from "../../services/template.service";
import { Template } from "../../models/template";
import { ToastService } from "ts-ui/toast";
import { LoggerService } from "ts-ui/logger";
import { Objects } from "ts-ui/helper";
import { StorageService } from "../../services/storage.service";
import { JsonObject, Severity } from "../../models/common";
import { AgTable, TableColumn, TableOption } from "ts-ui/ag-table";
import { AgCellColor } from "./renderer";
import { GetRowIdParams } from "@ag-grid-community/core";
import { TicketFormComponent } from "../ticket-form/ticket-form.component";
import { Observable, Observer } from "rxjs";
import { Router } from "@angular/router";
import {ModalService} from "../../services/ui/model.service";

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

const defaultInfo: Partial<Template> = {
    text_color: '#475569',
    'bg_color': '#f1f5f9'
};

@Component({
    selector: "ts-template-form",
    templateUrl: "./template.component.html",
    styleUrl: "./template-form.component.scss",
    encapsulation: ViewEncapsulation.None,
})
export class TemplateFormComponent implements OnInit {

    get template(): Partial<Template> {
        const json = Objects.extractValueNotNull(this.formGroup?.getRawValue());
        return { title: 'Hiển thị', ...json };
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
    rows: Template[] = [];
    lsSeverity: Severity[] = ['primary', 'secondary', 'success', 'info', 'danger', 'help', 'warning', 'contrast'];
    lsColor: any[] = [{label: '', color: ''}];
    lsEntity: string[] = ['form_ticket'];

    columns: TableColumn[] = [
        { field: 'entity_code', headerName: 'Mã Form', width: 131, rowDrag: false, headerCheckboxSelection: true, checkboxSelection: true },
        { field: 'icon', headerName: 'Icon', width: 70 },
        { field: 'title', headerName: 'Tiêu đề' },
        { field: 'bg_color', headerName: 'Màu nền', width: 87, cellRenderer: AgCellColor },
        { field: 'text_color', headerName: 'Màu chữ', width: 87, cellRenderer: AgCellColor },
        { field: 'data', headerName: 'Dữ liệu', width: 117, cellRenderer: (data: any) => data.value ? 'Có dữ liệu' : '' },
        { field: 'summary', headerName: 'Diễn giải' }
    ];

    agOption: TableOption = {
        defaultColDef: {
            suppressHeaderMenuButton: true,
        },
        rowSelection: 'multiple',
        onRowSelected: () => this.state.visibleDelete = this.agTable.getSelectedRows().length > 0,
        getRowId: (params: GetRowIdParams<Template>) => `ROWID_${params.data?.template_id}`,
        //onRowDragEnd: this.onChangeRowPosition.bind(this)
    };

    @ViewChild(AgTable)
    agTable: AgTable;

    @Input({alias: 'entity'})
    entityCode: string;

    @Input()
    lastUrl: string;

    constructor(
        private fb: FormBuilder,
        private toast: ToastService,
        private config: StorageService,
        private logger: LoggerService,
        private router: Router,
        private def: ChangeDetectorRef,
        private modal: ModalService,
        private templateSrv: TemplateService) {
        this.createFormGroup();
    }

    createFormGroup() {
        this.formGroup = this.fb.group({
            icon: [null],
            template_id: [null],
            entity_code: [null, Validators.required],
            title: [null, Validators.required],
            summary: [null],
            bg_color: [null],
            text_color: [null],
            style: [null],
            color: [null],
            data: [null, Validators.required],
            severity: [null],
            edit_data: [true],
            is_default: [false],
            position: [null]
        });

        this.pathValue({ ...defaultInfo });


    }

    ngOnInit() {
        this.pathValue({entity_code: this.entityCode});
        this.loadTemplate();
    }

    validateTitle(): Observable<any> {
        const { title, template_id } = this.template;
        return new Observable((obs: Observer<boolean>) => {

            this.agTable.tableApi.forEachNode(node => {
                const nTitle = node.data?.title ?? '';
                const nId = node.data?.template_id ?? '';
                if (nTitle === title && nId !== template_id) {
                    obs.error(title);
                    obs.complete();
                }
            });

            obs.complete();
        });

    }

    createTemplate() {
        this.resetForm();
        this.state.visibleCopy = false;
        this.state.visibleDelete = false;
        this.formGroup.get('entity_code').enable();
    }

    copyTemplate(): void {
        this.state.visibleDelete = false;
        this.resetForm({ ...this.template,
            template_id: undefined, title: undefined
        });
    }


    loadTemplate() {
        this.state.asyncTemplate = true;
        this.state.visibleDelete = false;

        const entities = notBlank(this.entityCode) ? [this.entityCode] : [];
        this.templateSrv.getAllByUser(...entities).subscribe({
            next: (page) => {
                this.rows = page.data;
                this.state.asyncTemplate = false;
                this.toast.success(this.config.i18n.loadTemplateOk );
            },
            error: (err) => {
                this.state.asyncTemplate = false;
                this.logger.error(`loadTemplate error: `, err);
            },
        });
    }

    saveTemplate() {

        if (this.formGroup.invalid) {
            this.toast.warning( this.config.i18n.form_invalid );
            return;
        } 
        else {
            this.validateTitle().subscribe({
                error: title => {
                    this.toast.warning( `Tiêu đề [${title}] đã tồn tại. Vui lòng nhập lại.` );
                },
                complete: () => {
                    this.state.asyncSave = true;

                    const formValue: Template = this.formGroup.getRawValue();
                    formValue.data = JSON.parse(formValue.data ?? '{}');

                    const isNew = isNull(formValue.template_id);

                    this.templateSrv.create(formValue).subscribe({ 
                        next: (template: Template) => {
                            this.state.asyncSave = false;
                            if(isNew) this.agTable.addRows(template );
                            else this.agTable.updateRows(template);
                            this.toast.success( this.config.i18n.saveTemplateOk );
                            this.def.detectChanges();
                        }, 
                        error: (err) => {
                            this.state.asyncSave = false;
                            this.logger.error(`saveTemplate error: `, err);
                            this.def.detectChanges();
                        },
                    });
                }
            })
        }


    }

    deleteTemplate() {
        const rows: Template[] = this.agTable.getSelectedRows();
        for(const row of rows) {
            this.templateSrv.deleteById(row.template_id).subscribe({
                next: res => this.agTable.removeRows(row),
                error: err => this.toast.error(`Lỗi xóa mẫu ${row.title} => ${err}`)
            });
        }
    }

    selectTemplate(template: Template): void {
        this.pathValue(template);
        this.state.visibleCopy = true;
        this.formGroup.get('entity_code').disable();
        this.formGroup.get('data').disable();

        if (!this.lsEntity.includes(template.entity_code)) {
            this.toast.warning(`Mã mẫu ${template.entity_code} không tồn tại.` )
        }
    }

    selectColor(color: any) {}

    resetForm(data?: any): void {
        this.formGroup.reset({ ...defaultInfo, ...data });
    }

    settingData() {
        const entity = this.template.entity_code;
        if (isBlank(entity)) {
            this.toast.warning('Vui lòng chọn <b>[Mã mẫu]</b>' );
            return;
        }

        const template = Template.from(this.template);
        const openRef = this.modal.open(TicketFormComponent, {
            header: 'Cấu hình dữ liệu mặc định',
            closable: true, draggable: false, resizable: false,
            data: { template: template }
        });

        openRef.onClose.subscribe({
            next: res => {
                if (res && res.data) {
                    this.pathValue({ data: res.data });
                }
            }
        });


    }

    selectSeverity(severity: string): void {

    }

    onChangeRowPosition(event: any): void {
        console.log(this.rows)
    }

    previousPage(): void {
        this.router.navigate([this.lastUrl]);
    }

    private pathValue(value: Partial<Template>) {
        const data = value.data ? JSON.stringify(value.data, null, 0) : undefined;
        this.formGroup.patchValue({ ...value, data });
    }
}