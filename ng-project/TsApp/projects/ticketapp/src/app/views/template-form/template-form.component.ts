import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
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
import { Ticket } from "../../models/ticket";
import { TemplateService } from "../../services/template.service";
import { Template } from "../../models/template";
import { ToastService } from "../../services/toast.service";
import { LoggerService } from "ts-logger";
import { Objects } from "ts-helper";
import {StorageService} from "../../services/storage.service";


@Component({
  selector: "ts-template-form",
  templateUrl: "./template-form.component.html",
  styleUrl: "./template-form.component.scss",
  encapsulation: ViewEncapsulation.None,
})
export class TemplateFormComponent implements OnInit, OnChanges {
  //private _template: Template;

  @Input({ transform: booleanAttribute })
  disableList: boolean = true;

  @Input() set template(data: Template) {
    this.set_template(data);
  }

  @Input() entityCode: string;
  @Output() onNew = new EventEmitter();
  @Output() onSave = new EventEmitter();
  @Output() onDelete = new EventEmitter();


  instance: DynamicDialogComponent | undefined;
  templateMap: Map<number, Template> = new Map();
  //template: Template = new Template();

  formGroup: FormGroup;
  ticket: Ticket;

  asyncTemplate: boolean = false;
  asyncSave: boolean = false;
  labelSave: string = "save";

  entities = ['form_ticket'];

  get templates(): Template[] {

    return [...this.templateMap.values()];
  }

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
      template_id: [{ value: null, disabled: this.disableList }],
      entity_code: [null],
      title: [null, Validators.required],
      summary: [null, Validators.required],
      bg_color: ["#ffffff", Validators.required],
      text_color: ["#000000", Validators.required],
      data: [{ value: null, disabled: true }, Validators.required],
    });
  }

  ngOnInit() {
    console.log("ngOnInit");
    const refView = this.toast.getDialogComponentRef(this.ref);
    this.instance = refView && refView.instance;

    if (this.instance && this.instance.data) {
      this.ticket = this.instance.data;
    }

    // if(Objects.notNull(this._template)){
    //   this._template.entity_code = this.entityCode;
    //   this.formGroup.patchValue(this._template);
    // }

  }

  private set_template(data: Template) {
    this.formGroup.patchValue(data);
  }

  ngOnChanges(changes: SimpleChanges): void {
    //console.log(changes)
    //if('template' in changes) {
    //console.log(this._template)
    // this.formGroup.patchValue(changes['template'].currentValue);
    // }
  }

  resetForm(data: any = this.template): void {
    this.formGroup.reset({
      bg_color: "#ffffff",
      text_color: "#000000",
      ...data,
    });
  }

  closeDialog(): void {
    this.ref.close(this.ticket);
  }

  loadTemplate() {
    this.asyncTemplate = true;

    this.templateSrv.getAllByUser().subscribe({
      next: (page) => {
        this.templateMap = new Map(page.data.map((i) => [i.template_id, i]));
        this.asyncTemplate = false;
        this.toast.success({ summary: this.config.i18n.loadTemplateOk });
      },
      error: (err) => {
        this.asyncTemplate = false;
        this.logger.error(`loadTemplate error: `, err);
      },
    });
  }

  selectTemplate(template: Template) {
    //this.template = template ?? new Template();
    if (Objects.isEmpty(template)) this.resetForm();
    else this.set_template(template);
  }

  createNew() {
    this.template = new Template();
    this.resetForm();
  }

  saveTemplate() {
    if (this.formGroup.invalid) {
      this.toast.warning({ summary: this.config.i18n.form_invalid });
      return;
    }

    this.asyncSave = true;

    const formValue: Template = this.formGroup.getRawValue();
    console.log(formValue);

    this.templateSrv.save(formValue).subscribe({
      next: (template: Template) => {
        this.asyncSave = false;
        this.set_template(template);
        this.templateMap.set(template.template_id, template);
        this.toast.success({ summary: this.config.i18n.saveTemplateOk });
        this.def.detectChanges();
      },
      error: (err) => {
        this.asyncSave = false;
        this.logger.error(`saveTemplate error: `, err);
        this.def.detectChanges();
      },
    });
  }

  selectEntity(arg0: any) {}
}