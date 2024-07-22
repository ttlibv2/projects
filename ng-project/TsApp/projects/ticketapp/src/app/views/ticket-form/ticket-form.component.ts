import { ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewEncapsulation } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MenuItem } from "primeng/api";
import { Ticket, TicketTemplateData } from "../../models/ticket";
import { ITicketOption, TicketOption } from "../../models/ticket-option";
import { FindPartnerComponent } from "../find-partner/find-partner.component";
import { CatalogService } from "../../services/catalog.service";

import { Catalog } from "../../models/catalog";
import { ToastService } from "../../services/toast.service";
import { Objects, Asserts } from "ts-helper";
import { Software } from "../../models/software";
import { ClsTeam } from "../../models/od-cls";
import { LoggerService } from "ts-logger";
import { Observable, Observer, of, map } from "rxjs";
import { Chanel } from "../../models/chanel";
import { Template } from "../../models/template";
import * as cls from "../../models/od-cls";
import { CatalogComponent } from "../catalog/catalog.component";
import { User } from "../../models/user";
import { StorageService } from "../../services/storage.service";
import { DatePipe } from "@angular/common";
import { TicketUtil2 } from "./ticket-util";
import { Question } from "../../models/question";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { Router } from "@angular/router";
import { TicketService } from "../../services/ticket.service";

const { notNull, notEmpty, isEmpty, isNull } = Objects;

export type TicketState = 'new' | 'update' | 'delete';

export interface State {
  assign?: boolean;
  asyncLoadCate?: boolean;
  asyncSaveTicket?: boolean;
}

export interface OptionLabel {
  label?: string;
  hidden?: boolean;
  formControl?: string;
  command?: (event: any) => void;
}

export interface SaveTicketEvent {
  ticket: Ticket;
  state: TicketState;
}

export interface SetDataInput {
  data: Ticket;
  option?: {
    useDefault: boolean;
    templateId?: number;
  }
}

@Component({
  selector: "ts-ticket-form",
  templateUrl: "./ticket-form.component.html",
  styleUrl: "./ticket-form.component.scss",
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TicketFormComponent implements OnInit, OnChanges {

  get formRawValue(): Ticket {
    return this.ticketForm.getRawValue();
  }

  get templateTitle(): string {
    return !!this.currentTemplate ? ' - ' + this.currentTemplate.title : '';
  }

  get templates(): Template[] {
    return this.catalog?.ls_template?.get('form_ticket') ?? [];
  }

  get isEditTicket(): boolean {
    return false;
  }

  get isSubjectReadOnly(): boolean {
    const { emailTicket, autoFill } = this.options;
    return (emailTicket === false || autoFill === true);
  }

  get templateDefault(): Template {
    if (notNull(this.currentTemplate)) return this.currentTemplate;
    else return this.currentTemplate = this.catalog?.ls_template?.get_ticket_def();
  }

  // get options(): ITicketOption {
  //   return this.formRawValue['options'];
  // }

  readonly toolActions: MenuItem[] = [
    {
      label: "Tải danh mục",
      icon: "pi pi-home",
      command: (event) => this.loadCatalogs().subscribe(),
    },
    {
      label: "Xóa cache",
      icon: "pi pi-home",
      command: (event) => this.clearCache(),
    },
    {
      label: "Cập nhật mẫu",
      icon: "pi pi-home",
      command: _ => this.router.navigate(['/admin/templates'], {
        queryParams: {
          entity: 'form_ticket', lastUrl: '/admin/ticket-form'
        }
      }),
    },
    {
      label: "Sao chép mẫu",
      icon: "pi pi-home",
      command: _ => this.copyTicket(),
    },
  ];

  readonly labelOptions: OptionLabel[] = [
    {
      label: "Tự động tạo > Copy",
      formControl: "autoCreate",
      command: (checked) => (this.options.autoCreate = checked),
    },
    {
      label: "[TS] Tự động điền",
      formControl: "autoFill",
      command: (checked) => (this.options.autoFill = checked),
    },
    {
      label: "Hiển thị ALL",
      formControl: "viewAll",
      command: (checked) => (this.options.viewAll = checked),
    },
    {
      label: "Hiển thị cột TS24",
      formControl: "viewTs24",
      command: (checked) => (this.options.viewTs24 = checked),
    },
    {
      label: "Lưu Cache",
      formControl: "saveCache",
      command: (checked) => (this.options.saveCache = checked),
    },
    {
      label: "E-mail ticket",
      formControl: "emailTicket",
      hidden: false,
      command: (checked) => (this.options.emailTicket = checked),
    }
  ];

  state: State = {};
  ticketForm: FormGroup;
  asyncLoadCate: boolean = false;
  catalog: Catalog = new Catalog();
  lsSoftName: string[] = [];
  team_head: cls.ClsTeamHead;
  currentTemplate: Template;
  userLogin: User;
  utils: TicketUtil2;
  viewTemplate: boolean = false;
  hasBorderEnd: boolean = true;
  options: TicketOption = TicketOption.createDef();

  @Input()
  set data(input: Ticket) {
    if (notNull(input)) {
      this.ticketForm.reset();
      this.ticketForm.patchValue(input);
    }
  }

  @Output() onSave = new EventEmitter<SaveTicketEvent>();

  constructor(
    private fb: FormBuilder,
    private toast: ToastService,
    private storage: StorageService,
    private cref: ChangeDetectorRef,
    private ticketSrv: TicketService,
    private catalogSrv: CatalogService,
    private logger: LoggerService,
    private router: Router,
    private datePipe: DatePipe,
    private dialogRef: DynamicDialogRef) {
    this.createFormGroup();
  }

  ngOnInit() {
    const dialogInstance = this.toast.getDialogComponentRef(this.dialogRef)?.instance;
    if (dialogInstance && dialogInstance.data?.template) {
      this.viewTemplate = true;
      this.currentTemplate = dialogInstance.data.template;
    }

    this.userLogin = this.storage.loginUser;
    this.loadCatalogs(true, true).subscribe({
      next: _ => {
        this.createNew(this.currentTemplate);
      }
    });

  }

  ngOnChanges(changes: SimpleChanges): void {
  }

  createFormGroup(): void {
    this.ticketForm = this.fb.group({
      ticket_id: [null],
      ticket_on: [null],
      full_name: [null],
      tax_code: [null],
      company_name: [null],
      phone: [null],
      teamviewer: [null],
      customer_name: [null],
      content_required: [null],
      content_help: [null],
      reception_time: [null],
      complete_time: [null],
      content_copy: [null],
      email: [null],
      subject: [null],
      body: [null],
      note: [null],
      reply: [null],
      content_email: [null],
      group_help: [null, Validators.required],
      question: [null],
      software: [null, Validators.required],
      chanels: [null, Validators.required],
      support_help: [null, Validators.required],
      soft_name: [null, Validators.required],
      images: [null],
      od_assign: [null, Validators.required],
      od_category_sub: [null, Validators.required],
      od_category: [null, Validators.required],
      od_partner: [null],
      od_partner_id: null,
      od_priority: [null],
      od_repiled: [null, Validators.required],
      save_question: [false],
      od_subject_type: [null, Validators.required],
      od_tags: [null],
      od_team: [null, Validators.required],
      od_team_head: [{ value: null, disabled: true }],
      od_ticket_type: [null],
      od_topic: [null],
      options: this.fb.group({
        autoCreate: [null],
        autoFill: [null],
        viewAll: [null],
        viewTs24: [null],
        saveCache: [null],
        emailTicket: [null]
      }),

      edit_note: [true],
      edit_ticket: [true]
    });

    this.utils = new TicketUtil2(this);
    this.utils.registerListener();
  }

  loadCatalogs(autoLoad: boolean = false, useCache: boolean = false): Observable<any> {
    return new Observable((observer: Observer<any>) => {

      if (useCache === true && notNull(this.storage.allCatalog)) {
        this.catalog = this.storage.allCatalog;
        console.log(this.catalog)
        observer.next(this.catalog);
        observer.complete();
      }
      else {

        const cateRef = this.toast.openDialog(CatalogComponent, {
          header: 'Danh mục cần lấy ?',
          closeOnEscape: true,
          focusOnShow: false,
          data: { templateCode: 'form_ticket', autoLoad }
        });

        cateRef.onClose.subscribe({
          error: (err) => {
            this.asyncLoadCate = false;
            observer.error(err);
          },
          next: (res: Catalog) => {
            this.catalog = Objects.isNull(this.catalog) ? res : this.catalog.update(res);
            this.cref.detectChanges();

            observer.next(res);
            observer.complete();
          }
        })
      }
    });

  }

  clearCache() {
    this.storage.clearCache();
    this.catalogSrv.clearCache().subscribe();
  }

  selectTemplate(template: Template, checkData: boolean = true) {

    if (checkData === true && isEmpty(template.data)) {
      this.toast.warning({ summary: 'Mẫu chưa cấu hình dữ liệu' });
      return;
    }

    this.currentTemplate = template;
    const data: TicketTemplateData = template.data;
    if (notEmpty(data)) {
      this.templateToTicket(data).subscribe({
        error: err => console.error(err),
        next: json => {
          this.options = json.options;
          this.ticketForm.reset(json, { onlySelf: false, emitEvent: true });
        }
      });
    }
  }

  saveTemplate() {
    const formValue = this.ticketForm.getRawValue();
    const data: Ticket = Objects.extractValueNotNull(formValue);
    const json: TicketTemplateData = {
      chanel_ids: data.chanels?.map(c => c.id),
      software_id: data.software?.id,
      soft_name: data.soft_name ?? data.software?.soft_names[0],
      group_help_id: data.group_help?.id,
      support_help_id: data.support_help?.id ?? (data.chanels ?? [])[0]?.id,
      team_id: data.od_team?.id,
      assign_id: data.od_assign?.id,
      subject_type_id: data.od_subject_type?.id,
      repiled_id: data.od_repiled?.id,
      category_id: data.od_category?.id,
      category_sub_id: data.od_category_sub?.id,
      team_head_id: data.od_team_head?.id,
      ticket_type_id: data.od_ticket_type?.id,
      priority_id: data.od_priority?.id,
      tag_ids: data.od_tags?.map(t => t.id),
      options: { ...formValue.options }
    };
    this.dialogRef.close({ data: json });
  }

  resetTemplate(): void {
    if (this.currentTemplate) {
      this.createNew(this.currentTemplate);
    }
  }

  createNew(template?: Template) {
    template = template ?? this.get_defaultTemplate();
    this.logger.info('create new - use template ', template?.title);
    this.selectTemplate(template);

  }

  get_defaultTemplate(): Template {
    return this.currentTemplate ?? this.catalog.ls_template.get_ticket_def();
  }

  saveTicket() {
    const data: Ticket = this.ticketForm.getRawValue();
    data.chanel_ids = data.chanels?.map(c => c.id);
    data.company_name = data.od_partner?.company_name;
    data.template_id = this.currentTemplate?.template_id;
    data.source = data.source ?? 'tsweb';

    const isNew = isNull(data.ticket_id);
    const prefixLabel = isNew ? 'Tạo' : 'Cập nhật';

    this.ticketSrv.save(data).subscribe({
      error: err => {
        this.state.asyncSaveTicket = false;
        this.toast.error({ summary: `Đã xảy ra lỗi <b>[${prefixLabel}]</b> ticket -> ${err}` });
      },
      next: res => {
        this.toast.success({ summary: `[${res.ticket_id}] <b>[${prefixLabel}]</b> ticket thành công` });
        this.onSave.emit({ ticket: res, state: isNew ? 'new' : 'update' });
        this.pathValue(res);
      }
    });
  }

  searchUser() {
    const { tax_code, phone, email } = this.formRawValue;
    const ref = this.toast.openDialog(FindPartnerComponent, {
      header: "Tìm kiếm khách hàng",
      data: { vat: tax_code, mobile: phone, email: email },
    });

    ref.onClose.subscribe({
      next: (cls: cls.ClsPartner) => {
        this.utils.pathValueForm({
          tax_code: cls.vat,
          email: cls.email,
          phone: cls.phone,
          customer_name: cls.customer_name,
          od_partner_id: cls.customer_id,
          od_partner: cls
        });
      }
    });
  }

  onSelectSoftware(data: Software) {
    this.lsSoftName = notNull(data) ? data.soft_names : [];
    this.pathValue({ soft_name: this.lsSoftName[0] });
  }

  searchAssign(data: any) {
    //this.catalogSrv.searchAssign(data);
  }

  onSelectHelpdeskTeam(data: ClsTeam) {
    this.loadMemberOfTeam(data, true)
      // Optional.ofNullable(data.members).map((users) => of(users))
      //   .orElseGet(() => this.catalogSrv.searchAssignByIds(data.team_members))
      .subscribe({
        error: (_) => (this.state.assign = false),
        next: (members) => {
          this.state.assign = false;
          data.members = members;
          this.catalog.ls_assign = members;
          this.team_head = data?.team_head;
          this.pathValue({
            od_assign: undefined,
            od_team_head: data.team_head
          });
        },
      });
  }

  onClearChanels(event: any) {
    this.pathValue({ support_help: undefined });

  }

  onSelectChanel(data: Chanel[] = []): void {
    this.pathValue({ support_help: isEmpty(data) ? undefined : data[0] });
  }

  onSelectQuestion(question: Question): void {
    this.utils.pathValueForm({
      content_help: question.reply,
      content_required: question.title
    });
  }

  copyTicket(): void {
    this.utils.resetForm({ ...this.utils.formValue, ticket_id: undefined });
    this.saveTicket();
  }

  private pathValue(data: Partial<Ticket>, options?: { onlySelf?: boolean, emitEvent?: boolean }) {
    console.log('pathValue', data, options)
    this.ticketForm.patchValue(data, options);
  }

  private pathEditor(data: Partial<Ticket>): void {
    const values: any[] = Object.keys(data).map(k => [k, { html: data[k] }]);
    this.ticketForm.patchValue(Object.fromEntries(values), { emitEvent: false });
  }

  private loadMemberOfTeam(data: ClsTeam, stop: boolean = false): Observable<cls.ClsAssign[]> {
    if (notEmpty(data.members)) return of(data.members);
    else if (notEmpty(data.team_members)) return this.catalogSrv.searchAssignByIds(data.team_members);
    else if (stop === false) return this.loadMemberOfTeam(this.catalog.ls_helpdesk_team.find(t => t.id === data.id), true);
    else return of([]);

  }

  private templateToTicket(data: TicketTemplateData): Observable<Partial<Ticket>> {
    data = Asserts.notNull(data, "TicketTemplateData is empty");
    const clsTeam = this.catalog.ls_helpdesk_team.find(s => data.team_id === s.id);

    return this.loadMemberOfTeam(clsTeam, false).pipe(map((members: cls.ClsAssign[]) => {
      const software = this.catalog.ls_software.find(s => data.software_id === s.id);
      const chanels = this.catalog.ls_chanel.filter(c => data.chanel_ids?.includes(c.id));
      const support_help = chanels?.find(c => data.support_help_id === c.id) ?? chanels[0];
      const assign = members?.find(s => data.assign_id === s.id);
      const now = new Date().getTime();

      this.lsSoftName = software?.soft_names ?? [];
      this.catalog.ls_assign = members;
      this.catalog.ls_team_head = [clsTeam?.team_head];

      return {
        chanels: chanels, software: software,
        soft_name: data.soft_name ?? software?.soft_names[0],
        support_help: support_help,
        group_help: this.catalog.ls_group_help.find(s => data.group_help_id === s.id),

        od_assign: assign,
        od_ticket_type: this.catalog.ls_ticket_type.find(s => data.ticket_type_id === s.id),
        od_subject_type: this.catalog.ls_subject_type.find(s => data.subject_type_id === s.id),
        od_repiled: this.catalog.ls_repiled_status.find(s => data.repiled_id === s.id),
        od_category: this.catalog.ls_category.find(s => data.category_id === s.id),
        od_category_sub: this.catalog.ls_category_sub.find(s => data.category_sub_id === s.id),
        od_team: clsTeam, od_team_head: clsTeam?.team_head,
        od_priority: this.catalog.ls_priority.find(s => data.priority_id === s.id),
        od_tags: this.catalog.ls_ticket_tag.filter(s => data.tag_ids?.includes(s.id)),
        options: TicketOption.from(data.options),

        reception_time: this.datePipe.transform(now, 'yyyyMMdd-HH:mm'),
        complete_time: this.datePipe.transform(now, 'yyyyMMdd-HH:'),

        ticket_on: this.datePipe.transform(now, 'yyMMdd'),
        full_name: this.userLogin.full_name

      };
    }))

  }

}