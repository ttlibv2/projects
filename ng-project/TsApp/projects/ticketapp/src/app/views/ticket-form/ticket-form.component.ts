import { booleanAttribute, ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, 
  Input, OnChanges, OnInit, Output, SimpleChanges, ViewEncapsulation } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { MenuItem, OverlayOptions } from "primeng/api";
import { Ticket } from "../../models/ticket";
import { TicketOption } from "../../models/ticket-option";
import { FindPartnerComponent } from "../find-partner/find-partner.component";
import { CatalogService } from "../../services/catalog.service";

import { Catalog } from "../../models/catalog";
import { ToastService } from "ts-ui/toast";
import { Objects, Asserts } from "ts-ui/helper";
import { Software } from "../../models/software";
import { ClsTeam } from "../../models/od-cls";
import { LoggerService } from "ts-ui/logger";
import { Observable, Observer, of, map } from "rxjs";
import { Chanel } from "../../models/chanel";
import * as cls from "../../models/od-cls";
import { CatalogComponent } from "../catalog/catalog.component";
import { User } from "../../models/user";
import { StorageService } from "../../services/storage.service";
import { DatePipe } from "@angular/common";
import { TicketFormGroup } from "./ticket-util";
import { Question } from "../../models/question";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { Router } from "@angular/router";
import { TicketService } from "../../services/ticket.service";
import { routerUrl } from "../../constant";
import { ViewHtml } from "./email-ticket";
import {DefaultData, InputData, XslTemplate} from "../shared/xsl-template";
import { EmailTemplate, TicketTemplate, TicketTemplateData } from "../../models/template";
import {FormField} from '../../models/form-field';
import { Alert } from "ts-ui/alert";
import { ModalService } from "ts-ui/modal";

const { notNull, notEmpty, isEmpty, isNull, notBlank } = Objects;

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
export class TicketFormComponent implements OnInit {


  get templateTitle(): string {
    return !!this.currentTemplate ? ' - ' + this.currentTemplate.title : '';
  }

  get data(): Partial<Ticket> {
    return this.forms.formValue;
  }


  get isEditTicket(): boolean {
    return false;
  }

  get isSubjectReadOnly(): boolean {
    const { emailTicket, autoFill } = this.options;
    return (emailTicket === false || autoFill === true);
  }

  get templateDefault(): TicketTemplate {
    if (notNull(this.currentTemplate)) return this.currentTemplate;
    else  return this.catalog?.ls_ticket_template?.get_default();
  }

  get emailTemplates(): EmailTemplate[] {
    return this.catalog?.get_email() || [];
  }

  readonly toolActions: MenuItem[] = [
    {
      label: "Xóa cache",
      icon: "pi pi-home",
      command: (event) => this.clearCache(),
    },
    {
      label: "Cập nhật mặc định",
      icon: "pi pi-home",
      command: _ => this.viewTemplateSetting(),
    },
    {
      label: "Nạp dữ liệu",
      icon: "pi pi-file-excel",
      command: _ => this.importXsl(),
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
    // {
    //   label: "Lưu Cache",
    //   formControl: "saveCache",
    //   command: (checked) => (this.options.saveCache = checked),
    // },
    {
      label: "E-mail ticket",
      formControl: "emailTicket",
      hidden: false,
      command: (checked) => {
        this.options.emailTicket = checked;
        this.toast.closeAll();
      },
    }
  ];


  @Input({ transform: booleanAttribute })
  hasBorderEnd: boolean = false;

  state: State = {};
  //ticketForm: FormGroup;
  asyncLoadCate: boolean = false;
  catalog: Catalog = new Catalog();
  lsSoftName: string[] = [];
  currentTemplate: TicketTemplate;
  userLogin: User;
  viewTemplate: boolean = false
  //emailTemplate: EMailTe

  options: TicketOption = TicketOption.createDef();
  ticketIsSend: boolean = false;
  forms: TicketFormGroup;

  // use for dialogRef
  overlayOptions: OverlayOptions;


  @Input()
  set data(input: Ticket) { 
    if (notNull(input)) {
      this.forms.resetForm(input);
      this.ticketIsSend = input.hasSend();
    }
  }

  @Output() onSave = new EventEmitter<SaveTicketEvent>();

  constructor(
    private fb: FormBuilder,
    private alert: Alert,
    private modal: ModalService,
    private toast: ToastService,
    private storage: StorageService,
    private cref: ChangeDetectorRef,
    private ticketSrv: TicketService,
    private catalogSrv: CatalogService,
    private logger: LoggerService,
    private router: Router,
    private datePipe: DatePipe,
    private dialogRef: DynamicDialogRef) {
      this.initFormGroup();

  }

  private initFormGroup() {
    this.forms = new TicketFormGroup(this, this.fb);
    this.forms.initialize();
    this.forms.registerListener();
  }

  ngOnInit() {


    const dialogInstance = this.modal.getInstance(this.dialogRef);
    if (dialogInstance && dialogInstance.data?.template) {
      this.viewTemplate = true;
      this.currentTemplate = TicketTemplate.from(dialogInstance.data.template);
      this.overlayOptions = {mode: 'modal'};
    }

    this.userLogin = this.storage.loginUser;
    this.loadCatalogs(true, true).subscribe({
      next: _ =>  this.createNew(this.currentTemplate)
    });

  }


  viewResultTicket(): void {

  }

  asyncLoadCatalogs(): void {
    this.loadCatalogs().subscribe();
  }

  loadCatalogs(autoLoad: boolean = false, useCache: boolean = false): Observable<any> {
    return new Observable((observer: Observer<any>) => {

      if (useCache === true && notNull(this.storage.catalog)) {
        this.catalog = this.storage.catalog;
        observer.next(this.catalog);
        observer.complete();
      }
      else {

        const cateRef = this.modal.open(CatalogComponent, {
          header: 'Danh mục cần lấy ?',
          width: '700px',
          data: { templateCode: ['ticket_template', 'email_template'], autoLoad }
        });

        cateRef.onClose.subscribe({
          error: (err) => {
            this.asyncLoadCate = false;
            observer.error(err);
          },
          next: (res: Catalog) => {
            this.catalog = isNull(this.catalog) ? res : this.catalog.update(res);
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

  selectTemplate(template: TicketTemplate, checkData: boolean = true) {

    if (isNull(template) || (checkData === true && isEmpty(template.data))) {
      // const ref = this.alert.warning({
      //   title: 'Thông báo !!',
      //   summary: 'Bạn chưa cấu hình dữ liệu mặc định cho Ticket. Bạn có muốn cấu hình luôn không ?',
      //   actions: [
      //     {label: 'Cấu hình', onClick: e => e.dynamicRef.close('ok')},
      //     {label: 'Hủy', onClick: e => e.dynamicRef.close('cancel')}
      //   ]
      // });

      // ref.onClose.subscribe(btn => {
      //   if("ok" == btn) this.viewTemplateSetting();
      // });

    }
    else {
      this.currentTemplate = template;
      const data = template.data;
      if (notEmpty(data)) {
        this.templateToTicket(data).subscribe({
          error: err => console.error(err),
          next: json => {
            this.options = json.options;
            this.forms.resetForm(json, { onlySelf: false, emitEvent: true });
          }
        });
      }
    }

   
  }

  saveTemplate() {
    const formValue = this.forms.formValue;
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
      replied_id: data.od_replied?.id,
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

  closeDialog(): void {
    this.dialogRef?.destroy();
  }

  createNew(template?: TicketTemplate) {
    template = template ?? this.catalog?.ls_ticket_template?.get_default();
    this.logger.info('create new - use template ', template?.title);
    this.selectTemplate(template);

  }

  saveTicket() {
    if (this.forms.invalid) {
      this.alert.warning({ title: 'Cảnh báo !!', summary: 'Vui lòng nhập đầy đủ thông tin' });
      return;
    }

    this.state.asyncSaveTicket = true;
    const waitRef = this.toast.loading('Đang lưu thông tin. Vui lòng đợi....');


    const data: Partial<Ticket> = this.forms.formValue;
    data.chanel_ids = data.chanels?.map(c => c.id);
    data.company_name = data.od_partner?.company_name;
    data.template_id = this.currentTemplate?.template_id;
    data.email_template = data.email_template;
    data.source = data.source ?? 'tsweb';

    const isNew = isNull(data.ticket_id);
    const prefixLabel = isNew ? 'Tạo' : 'Cập nhật';

    this.ticketSrv.save(data).subscribe({
      error: err => {
        this.state.asyncSaveTicket = false;
        this.toast.close(waitRef);
        this.toast.error(`Đã xảy ra lỗi <b>[${prefixLabel}]</b> ticket -> ${err}`);
      },
      next: res => {
        this.toast.close(waitRef);
        this.toast.success(`[${res.ticket_id}] <b>[${prefixLabel}]</b> ticket thành công`);
        this.onSave.emit({ ticket: res, state: isNew ? 'new' : 'update' });
        this.state.asyncSaveTicket = false;
        this.pathValue(res);
      }
    });
  }

  searchUser() {
    const { tax_code, phone, email } = this.forms.formValue;
    const ref = this.modal.open(FindPartnerComponent, {
      header: "Tìm kiếm khách hàng",
      data: { vat: tax_code, mobile: phone, email: email },
    });

    ref.onClose.subscribe({
      next: (cls: cls.ClsPartner) => {
        cls && this.forms.pathValue({
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

  changeOdTeam(data: ClsTeam) {
    this.logger.log('onSelectHelpdeskTeam', data);

    if (isNull(data)) {
      this.catalog.ls_assign = [];
      this.catalog.ls_team_head = [];
      this.pathValue({
        od_assign: undefined,
        od_team_head: undefined
      });
    }


    else this.loadMemberOfTeam(data, true).subscribe({
      error: (_) => (this.state.assign = false),
      next: (members) => {
        this.state.assign = false;
        data.members = members;
        this.catalog.ls_assign = members;
        this.catalog.ls_team_head = [data.team_head];
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
    if(this.options.autoFill == true){
      this.pathValue({ support_help: isEmpty(data) ? undefined : data[0] });
    }
  }

  onSelectQuestion(question: Question): void {
    this.forms.pathValue({
      content_help: question.reply,
      content_required: question.title
    });
  }

  copyTicket(): void {
    this.forms.copyValue();
    this.saveTicket();
  }

  viewTemplateSetting(): void {
    console.log(this.router.getCurrentNavigation());

    this.router.navigate([routerUrl.template], {
      queryParams: {
        thread: 'ticket_template', 
        lastUrl: routerUrl.form_ticket
      }
    })
  }

  private pathValue(data: Partial<Ticket>, options?: { onlySelf?: boolean, emitEvent?: boolean }) {
    console.log('pathValue', data, options)
    this.forms.pathValue(data, options);
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
        od_replied: this.catalog.ls_replied_status.find(s => data.replied_id === s.id),
        od_category: this.catalog.ls_category.find(s => data.category_id === s.id),
        od_category_sub: this.catalog.ls_category_sub.find(s => data.category_sub_id === s.id),
        od_team: clsTeam, od_team_head: clsTeam?.team_head,
        od_priority: this.catalog.ls_priority.find(s => data.priority_id === s.id),
        od_tags: this.catalog.ls_ticket_tag.filter(s => data.tag_ids?.includes(s.id)),
        options: TicketOption.from(data.options),

        reception_time: this.datePipe.transform(now, 'yyyyMMdd-HH:mm'),
        complete_time: this.datePipe.transform(now, 'yyyyMMdd-HH:'),

        ticket_on: this.datePipe.transform(now, 'yyMMdd'),
        full_name: this.userLogin.ts_name

      };
    }))

  }


  //======================
  emailFields: FormField[] = [];

  changeEmailTemplate(template: EmailTemplate): void {
    if(isNull(template)) {
      this.setupFieldEmailTemplate([]);
      this.emailFields = [];
    }
    else if(notBlank(template.data?.html)){
      const {fields} = template.data;
      this.emailFields = fields;
      this.setupFieldEmailTemplate(fields);
    }
  }

  get emailObject(): FormGroup {
    return this.forms.formGroup.get('email_object') as FormGroup;
  }

  setupFieldEmailTemplate(fields: FormField[]) {
    const emailGroup: FormGroup = this.emailObject;

    // remove current field
    Object.keys(emailGroup.controls).forEach(c => emailGroup.removeControl(c));

    // add field to group
    fields.forEach(field => {
      const {name, value, required} = field;
      const control = this.fb.control(value);
      if(required == true) control.addValidators(Validators.required);
      emailGroup.addControl(name, control);
    });

  }

  openEmailTemplate(): void {
    const { email_template, email_object } = this.forms.formValue;
    let html = email_template.data.html;

    html = html.replace(/\{\{(\S+)\}\}/g, (sub, arg) => {
      const value = email_object[arg];
      return Objects.isBlank(value) ? `{{${arg}}}` : value;
    });
    
    this.modal.open(ViewHtml, {
      header: 'Hiển thị mẫu',
      closable: true,
      maximizable: true,
      dismissableMask: true,
      data: { html }
    });

  }

  set_data(data: Partial<Ticket>, template: TicketTemplate, submit?:boolean) {
    const defaultData = this.templateToTicket(template.data);
    this.data = Objects.mergeDeep(defaultData, data);
    if(submit == true) this.saveTicket();
  }


  importXsl(): void {
    const ref = this.modal.open(XslTemplate, {
      header: 'Nạp dữ liệu ticket',
      width: '630px',
      data: {
        files: [
          // {
          //   label: 'Mẫu mặc định',
          //   name: 'ticket_default.xlsx',
          //   link: 'ticket_default.xlsx',
          //   selected: false, sheets: ['data']
          // },
          {
            label: 'Mẫu DS Lỗi',
            name: 'ticket_ds_loi.xlsx',
            link: 'ticket_ds_loi.xlsx',
            selected: true, sheets: ['data', 'data2']
          },
        ],
        defaults: this.catalog?.get_ticket().map(t=> ({label: t.title, item: t}) as DefaultData)
      } as InputData
    });
    //
    ref.onClose.subscribe({
      next: (data: any) => {
        console.log(data)
      }
    })


  }


}