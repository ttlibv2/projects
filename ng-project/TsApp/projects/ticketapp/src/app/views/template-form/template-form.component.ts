import {
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
  ViewChild,
  ViewEncapsulation,
  booleanAttribute,
} from "@angular/core";
import {
  DynamicDialogComponent,
  DynamicDialogRef,
} from "primeng/dynamicdialog";
import {
  FormBuilder,
  FormGroup,
  Validators,
} from "@angular/forms";
import { TemplateService } from "../../services/template.service";
import { Template } from "../../models/template";
import { ToastService } from "../../services/toast.service";
import { LoggerService } from "ts-logger";
import { Objects } from "ts-helper";
import {StorageService} from "../../services/storage.service";
import { JsonObject } from "../../models/common";
import { AgTableComponent, TableColumn, TableOption, TableRowClick } from "ts-ui/ag-table";
import { AgCellColor } from "./renderer";
import { GetRowIdParams, IRowNode, RowNode } from "ag-grid-community";
import { TicketFormComponent } from "../ticket-form/ticket-form.component";

const {notEmpty, notBlank, isBlank} = Objects;

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
}

@Component({
  selector: "ts-template-form",
  templateUrl: "./template-form.component.html",
  styleUrl: "./template-form.component.scss",
  encapsulation: ViewEncapsulation.None,
})
export class TemplateFormComponent implements OnInit {

  get template(): Partial<Template> {
    return this.formGroup?.getRawValue();
  }
  
  get hasPreview(): boolean {
    return notBlank(this.formGroup?.get('title').value);
  }

  formGroup: FormGroup;
  state: State = {};
  labelSave: string = "save";
  rows: Template[] = []; 
  lsEntity: string[] = ['form_ticket'];

  columns: TableColumn[] = [
    {field: 'entity_code', headerName: 'Mã Form', width: 121},
    {field: 'icon', headerName: 'Icon', width: 70},
    {field: 'title', headerName: 'Tiêu đề'},
    {field: 'bg_color', headerName: 'Màu nền', width: 87, cellRenderer: AgCellColor},
    {field: 'text_color', headerName: 'Màu chữ', width: 87 , cellRenderer: AgCellColor},
    {field: 'data', headerName: 'Dữ liệu', width: 117, cellRenderer: (data: any) => data.value ? 'Có dữ liệu' : ''},
    {field: 'summary', headerName: 'Diễn giải'}
  ];

  agOption: TableOption = {
    defaultColDef: {
      suppressHeaderMenuButton: true,
    },
    getRowId: (params: GetRowIdParams<Template>) =>  `ROWID_${params.data?.template_id}`
  };

  @ViewChild(AgTableComponent)
  agTable: AgTableComponent;

  constructor(
    private fb: FormBuilder,
    private toast: ToastService,
    private config: StorageService,
    private logger: LoggerService,
    private def: ChangeDetectorRef,
    private templateSrv: TemplateService ) {
    this.createFormGroup();
  }

  createFormGroup() {
    this.formGroup = this.fb.group({
      icon: [null],
      template_id: [null],
      entity_code: [this.lsEntity[0], Validators.required],
      title: [null, Validators.required],
      summary: [null, Validators.required],
      bg_color: ["#ffffff", Validators.required],
      text_color: ["#000000", Validators.required],
      data: [null, Validators.required],
      edit_data: [true]
    });
  }

  ngOnInit() {
    this.loadTemplate();
  }

  createTempate() {  
    this.resetForm();
    this.formGroup.get('entity_code').enable();

  }

  loadTemplate() {
    this.state.asyncTemplate = true;

    this.templateSrv.getAllByUser().subscribe({
      next: (page) => {
        this.rows = page.data;
        this.state.asyncTemplate = false;
        this.toast.success({ summary: this.config.i18n.loadTemplateOk });
      },
      error: (err) => {
        this.state.asyncTemplate = false;
        this.logger.error(`loadTemplate error: `, err);
      },
    });
  }

  saveTemplate() {

    if (this.formGroup.invalid) {
      this.toast.warning({ summary: this.config.i18n.form_invalid });
      return;
    }

    this.state.asyncSave = true;

    const formValue: Template = this.formGroup.getRawValue();
    formValue.data = JSON.parse(formValue.data ?? '{}');

    this.templateSrv.save(formValue).subscribe({
      next: (template: Template) => {
        this.state.asyncSave = false;
        this.agTable.updateRows(template);
        this.toast.success({ summary: this.config.i18n.saveTemplateOk });
        this.def.detectChanges();
      },
      error: (err) => {
        this.state.asyncSave = false;
        this.logger.error(`saveTemplate error: `, err);
        this.def.detectChanges();
      },
    });
  }

  deleteTemplate() {}

  selectTemplate(template: Template): void {
    const data = JSON.stringify(template?.data, null, 0);
    this.formGroup.patchValue({...template, data});
    this.formGroup.get('entity_code').disable();
    this.formGroup.get('data').disable();

    if(!this.lsEntity.includes(template.entity_code)) {
      this.toast.warning({summary: `Mã mẫu ${template.entity_code} không tồn tại.`})
    }
  }

  
  resetForm(data?: any): void {
    this.formGroup.reset({
      bg_color: "#ffffff",
      text_color: "#000000",
      ...data
    });
  }

  settingData() {
    const entity = this.template.entity_code;
    if(isBlank(entity)) {
      this.toast.warning({summary: 'Vui lòng chọn <b>[Mã mẫu]</b>'});
      return;
    }

    this.toast.openDialog(TicketFormComponent, {
     data: {item: this.template}
    });

    

  }
}