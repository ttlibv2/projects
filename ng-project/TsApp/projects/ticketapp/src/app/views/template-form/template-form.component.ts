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

const {isEmpty} = Objects;

interface TemplateInput {
  currentEntity?: string;
  currentTemplate?: Template;
  templates?: Template[];
  entities?: string[];
  data?: JsonObject;
}

interface State {
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
  
  get hasInstance(): boolean {
    return Objects.notNull(this.instance);
  }
    
  get hasPreview(): boolean {
    return Objects.notBlank(this.template?.title);
  }

  instance: DynamicDialogComponent | undefined;
  formGroup: FormGroup;
  state: State = {};
  labelSave: string = "save";
  rows: Template[] = []; 
  inputData: TemplateInput;

  @Input() entities: string[] = [];

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
    private ref: DynamicDialogRef,
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
      entity_code: [null, Validators.required],
      title: [null, Validators.required],
      summary: [null, Validators.required],
      bg_color: ["#ffffff", Validators.required],
      text_color: ["#000000", Validators.required],
      data: [null, Validators.required],
      edit_data: [true]
    });

   // this.formGroup.valueChanges.subscribe({next: res => this.state.hasChange = true});
  }

  ngOnInit() {
    console.log("ngOnInit");
    this.loadTemplate();

    const refView = this.toast.getDialogComponentRef(this.ref);
    this.instance = refView && refView.instance;

    if (this.instance && this.instance.data) {
      this.inputData = this.instance.data;
    }

  }

  resetForm(): void {
    this.formGroup.reset({
      bg_color: "#ffffff",
      text_color: "#000000",
    });
  }

  closeDialog(): void {
    //this.ref.close(this.ticket);
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

  createNew() {
    this.resetForm();
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

  clickEditData(checked: boolean): void { }

  selectTemplate(template: Template): void {
    const data = JSON.stringify(template?.data, null, 0);
    this.formGroup.patchValue({...template, data});
    this.formGroup.get('entity_code').disable();
    this.formGroup.get('data').disable();
  }

}