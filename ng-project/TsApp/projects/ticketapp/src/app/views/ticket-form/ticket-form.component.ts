import {
  booleanAttribute, ChangeDetectorRef, Component, effect, EventEmitter,
  inject,
  Input, OnDestroy, OnInit, Output, signal, ViewEncapsulation
} from "@angular/core";
import { MenuItem, OverlayOptions } from "primeng/api";
import { Ticket } from "../../models/ticket";
import { TicketOption } from "../../models/ticket-option";
import { FindPartnerComponent, openDialog } from "../find-partner/find-partner.component";
import { CatalogService } from "../../services/catalog.service";

import { Catalog } from "../../models/catalog";
import { ToastService } from "ts-ui/toast";
import { Objects, Asserts, AssignObject, Page } from "ts-ui/helper";
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
import { Question } from "../../models/question";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { Router } from "@angular/router";
import { TicketService } from "../../services/ticket.service";
import { Urls } from "../../constant";
import { ViewHtml } from "./email-ticket";
import { openXslTemplate, ReturnData } from "../shared/select-file/xsl-template";
import { EmailTemplate, TemplateThread, TicketTemplate, TicketTemplateData } from "../../models/template";
import { Alert } from "ts-ui/alert";
import { ModalService } from "ts-ui/modal";
import { FormGroup, FormsBuilder } from "ts-ui/forms";
import { Utils } from "./ticket-form-utils";
import { ImportData } from "./import-data";
import { OdPartnerService } from "../../services/od-partner.service";
import { AuthService } from "../../services/auth.service";
import { InputOption, SearchData as SearchUserData } from "../find-partner/partner.interface";
import { Platform } from "@angular/cdk/platform";
import { TestService } from "../../services/test.service";
const { notNull, notEmpty, isEmpty, isBlank, isNull, notBlank, isTrue, isFalse } = Objects;
export type TicketState = 'add' | 'update' | 'delete' | undefined;

export interface State {
  assign?: boolean;
  asyncLoadCate?: boolean;
  asyncSaveTicket?: boolean;

  visibleToolImport?: boolean;
}

export interface OptionLabel {
  label?: string;
  hidden?: boolean;
  formControl?: string;
  command?: (event: any) => void;
}

interface TicketEvent {
  ticket: Partial<Ticket>;
  state: TicketState;
}

export interface SaveTicketEvent extends TicketEvent {
  state: 'add' | 'update';
}

export interface DeleteTicketEvent extends TicketEvent {
  state: 'delete';
  result: any;
}

export interface SetDataInput {
  data: Ticket;
  option?: {
    useDefault: boolean;
    templateId?: number;
  }
}

export interface DsLoiItem {
  [field: string]: any;
  masothue: string;
  cqquanly: string;
  dienthoai: string;
  email: string;
  tenhoso: string;
  loaihoso: string;
  magiaodich: string;
  sohoso: string;
  ngaybatdau: string;
  maloigiaodich: string;
  motaloigiaodich: string;
}

export interface SearchUserOption extends InputOption {
    autoSave?: boolean;
}


@Component({
  selector: "ts-ticket-form",
  templateUrl: "./ticket-form.component.html",
  styleUrl: "./ticket-form.component.scss",
  encapsulation: ViewEncapsulation.None
})
export class TicketFormComponent implements OnInit, OnDestroy {

  @Input({ transform: booleanAttribute })
  hasBorderEnd: boolean = false;

  @Input()
  set formData(input: Ticket) {
    this.inputTicket = input;
    this.visibleResult = notBlank(input?.ticket_number);
    if (notNull(input) && notNull(this.utils)) {
      this.utils?.reset(input, {});
      this.inputTicket = undefined;
      //this.ticketIsSend = input.hasSend();
    }
  }

  @Input({ transform: booleanAttribute, alias: 'viewall' })
  private inputViewAll: boolean | undefined = undefined;

  @Output()
  onSave = new EventEmitter<SaveTicketEvent>();

  @Output()
  onDelete = new EventEmitter<DeleteTicketEvent>();


  get isPC(): boolean {
    //const {ANDROID, IOS} = this.platform;
    //return (isNull(ANDROID) || isFalse(ANDROID)) && (isNull(IOS) && isFalse(IOS));
    return true;
  }


  get templateTitle(): string {
    return !!this.ticketTemplate ? ' - ' + this.ticketTemplate.title : '';
  }

  get formData(): Partial<Ticket> {
    return this.utils?.formData;
  }

  get isEditTicket(): boolean {
    return notNull(this.formData?.ticket_id);
  }

  get templateDefault(): TicketTemplate {
    if (notNull(this.ticketTemplate)) return this.ticketTemplate;
    else return this.catalog?.ls_ticket_template?.get_default();
  }

  get emailTemplates(): EmailTemplate[] {
    return this.catalog?.get_email() || [];
  }

  get form(): FormGroup {
    return this.utils?.form;
  }

  get isViewAll(): boolean {
    return this.utils?.options?.viewAll ?? this.inputViewAll;
  }

  readonly toolActions: MenuItem[] = [
    {
      label: "Ticket mẫu",
      icon: "pi pi-home",
      command: _ => this.viewTemplateSetting('ticket_template'),
    },
    {
      label: "E-mail mẫu",
      icon: "pi pi-at",
      command: _ => this.viewTemplateSetting('email_template'),
    },
    { separator: true, label: '1111' },
    {
      label: "Nạp danh sách lỗi",
      icon: "pi pi-file-excel",
      command: _ => this.openDialogImportXsl(),
    },
    { separator: true, label: '1111' },
    {
      label: 'Sao chép ticket',
      icon: 'pi pi-clone',
      command: _ => this.copyTicket()
    },
    {
      label: 'Tải danh mục',
      icon: 'pi pi-home',
      command: _ => this.asyncLoadCatalogs()
    },
    { separator: true, label: '1111' },
    {
      label: "Xóa cache",
      icon: "pi pi-trash",
      command: _ => this.clearCache(),
    },
    {
      label: "Đăng xuất",
      icon: "pi pi-sign-out",
      command: _ => this.signout(),
    }
  ];

  readonly labelOptions: OptionLabel[] = [
    {
      label: "Tự động tạo > Copy",
      formControl: "autoCreate",
    },
    {
      label: "[TS] Tự động điền",
      formControl: "autoFill",
    },
    {
      label: "Hiển thị ALL",
      formControl: "viewAll",
      command: e => this.clickViewAll(e)
    },
    {
      label: "Hiển thị cột TS24",
      formControl: "viewTs24",
    },
    {
      label: "E-mail ticket",
      formControl: "emailTicket",
      hidden: false,
    }
  ];


  state: State = { visibleToolImport: true };
  catalog: Catalog = new Catalog();
  lsSoftName: string[] = [];
  ticketTemplate: TicketTemplate;
  userLogin: User;
  viewTemplate: boolean = false
  inputTicket: Ticket;

  //options: TicketOption = TicketOption.createDef();
  //ticketIsSend: boolean = false;
  visibleResult: boolean = false;
  readonly utils: Utils = new Utils(this);

  // use for dialogRef
  overlayOptions: OverlayOptions;

  //------ import
  idata: ImportData | null = null;

  get newLabel(): string {
    return this.isEditTicket ? 'actions.update' : 'actions.save';
  }

  constructor(
    public readonly platform: Platform,
    public readonly fb: FormsBuilder,
    public readonly alert: Alert,
    public readonly modal: ModalService,
    public readonly toast: ToastService,
    public readonly ticketSrv: TicketService,
    public readonly odSrv: OdPartnerService,
    public readonly storage: StorageService,
    public readonly datePipe: DatePipe,
    private authSrv: AuthService,

    private cref: ChangeDetectorRef,

    private catalogSrv: CatalogService,
    private logger: LoggerService,
    private router: Router,

    private dialogRef: DynamicDialogRef) {
  }



  ngOnInit() {
    const dialogInstance = this.modal.getInstance(this.dialogRef);
    if (dialogInstance && dialogInstance.data?.template) {
      this.viewTemplate = true;
      this.ticketTemplate = TicketTemplate.from(dialogInstance.data.template);
      this.overlayOptions = { mode: 'modal' };
    }

    this.userLogin = this.storage.loginUser;
    this.utils.initialize({
      viewAll: this.inputViewAll ?? false
    });

    this.loadCatalogs({autoLoad: true, useCache: true}).subscribe({
      next: _ => {
        this.createNew(this.templateDefault);
        //this.openDialogImportXsl();
        if (notNull(this.inputTicket)) {
          this.utils.reset(this.inputTicket);
          this.inputTicket = undefined;
        }


        
      }
    });

  }

  ngOnDestroy(): void {
    this.idata?.onDestroy();
    this.dialogRef?.destroy();
  }


  viewResultTicket(): void {

  }

  asyncLoadCatalogs(): void {
    this.loadCatalogs().subscribe();
  }

  loadCatalogs(options?: {autoLoad?: boolean, useCache?: boolean}): Observable<any> {
    options = Object.assign({autoLoad: false, useCache: false}, options);

    return new Observable((observer: Observer<any>) => {

      if (options.useCache === true && notNull(this.storage.catalog)) {
        console.log(`loadCatalogs => from cache`);
        this.catalog = this.storage.catalog;
        observer.next(this.catalog);
        observer.complete();
      }
      else {

        const cateRef = this.modal.open(CatalogComponent, {
          header: 'Danh mục cần lấy ?',
          closable: false,
         // width: '700px',
          data: { 
            templateCode: ['ticket_template', 'email_template'], 
            autoLoad: options.autoLoad 
          }
        });

        cateRef.onClose.subscribe({
          error: (err) => {
            this.state.asyncLoadCate = false;
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

    const hasShow = isNull(template) || (checkData === true && isEmpty(template.data));
    if (isFalse(this.viewTemplate) && hasShow) {
      const ref = this.alert.warning({
        title: 'Thông báo !!',
        summary: 'Bạn chưa cấu hình dữ liệu mặc định cho Ticket. Bạn có muốn cấu hình luôn không ?',
        okLabel: 'Cấu hình',
        cancelClick: e => e.dynamicRef.destroy(),
        okClick: e => e.dynamicRef.close('ok'),
      });

      ref.onClose.subscribe(btn => {
        if ("ok" == btn) this.viewTemplateSetting('ticket_template');
      });

    }
    else {

      const func = () => {
        this.ticketTemplate = template;
        const data = template.data;
        if (notEmpty(data)) {
          this.templateToTicket(data).subscribe({
            error: err => console.warn(err.url, err.message),
            next: json => this.utils.reset(json, { func: `selectTemplate` })
          });
        }
      };

      // if (this.form.dirty) {
      //   let ref = this.alert.warning({
      //     title: 'Cảnh báo !!',
      //     summary: `Dữ liệu trên [form] đã thay đổi. Bạn có muốn cập nhật lại dữ liệu không ?`,
      //     actions: [
      //       {label: 'Thay đổi', onClick: e => e.dynamicRef.close('ok')},
      //       {label: 'Đóng', onClick: e => e.dynamicRef.close('cancel')}
      //     ]
      //   });
      //   ref.onClose.subscribe(act => {
      //     if (act === 'ok') func();
      //   });
      // }
      // else 
      func();
    }


  }

  saveTemplate() {
    const formValue = this.form.getRawValue();
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
      email_template_id: data.email_template?.template_id,
      options: { ...formValue.options }
    };
    this.dialogRef.close({ data: json });
  }

  resetTemplate(): void {
    if (this.ticketTemplate) {
      this.createNew(this.ticketTemplate);
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

  deleteTicket(): void {
    const { ticket_id, ticket_number } = this.formData;
    if (notNull(ticket_number)) {
      this.alert.warning({ title: 'Cảnh báo !!', summary: 'Ticket này đã được tạo trên hệ thống TS.' });
    }
    else {
      const ref = this.alert.warning({
        title: 'Cảnh báo !!',
        summary: 'Bạn muốn xóa ticket này ?',
        okClick: e => e.dynamicRef.close('ok'),
        cancelClick: e => e.dynamicRef.close('cancel')
      });

      ref.onClose.subscribe(act => {
        ref.destroy();
        if ('ok' === act) {
          this.ticketSrv.deleteById(ticket_id).subscribe({
            error: msg => this.toast.error(`[${ticket_id}] Không xóa được ticket này ==> ${msg}`),
            next: ok => {
              this.onDelete.emit({ state: 'delete', ticket: this.formData, result: ok });
              this.createNew();
            }
          });
        }
      });

    }



  }

  saveTicket() {
    if (this.form.invalid) {
      this.alert.warning({ title: 'Cảnh báo !!', summary: 'Vui lòng nhập đầy đủ thông tin' });
      return;
    }

    this.state.asyncSaveTicket = true;
    const waitRef = this.toast.loading('Đang lưu thông tin. Vui lòng đợi....');


    const data: Partial<Ticket> = this.form.getRawValue();
    data.chanel_ids = data.chanels?.map(c => c.id);
    data.company_name = data.od_partner?.company_name;
    data.template_id = this.ticketTemplate?.template_id;
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
        this.state.asyncSaveTicket = false;
        this.utils.reset(res, { func: `saveTicket` });
        this.onSave.emit({ ticket: res, state: isNew ? 'add' : 'update' });
      }
    });
  }

  searchUser(option?: SearchUserOption, customData?: SearchUserData) {
    const { tax_code, phone, email } = this.utils.formData;

    const data: SearchUserData = {
       operator: 'equal', ...customData, 
      vat: tax_code, mobile: phone, email: email 
    };

    const ref = openDialog(this.modal, data, option);

    ref.onClose.subscribe({
      next: (cls: cls.ClsPartner) => {
        if (notNull(cls)) {

          this.form.patchValue({
            tax_code: cls.vat,
            email: isBlank(email) ? cls.email : email,
            phone: isBlank(phone) ? cls.phone : phone,
            customer_name: cls.customer_name,
            od_partner_id: cls.customer_id,
            od_partner: cls
          });

          if (isTrue(option?.autoSave)) {
            this.saveTicket();
          }
        }

      }
    });
  }

  private handleAfterSearch(dialogRef: DynamicDialogRef, page: Page<cls.ClsPartner>): void {
    console.log(`handleAfterSearch: `, page);
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
    if (this.utils.auto_fill) {
      this.pathValue({ support_help: isEmpty(data) ? undefined : data[0] });
    }
  }

  onSelectQuestion(question: Question): void {
    this.form.patchValue({
      content_help: question.reply,
      content_required: question.title
    });
  }

  copyTicket(): void {
    this.utils.copyValue();
    this.saveTicket();
  }

  viewTemplateSetting(thread: TemplateThread): void {
    //console.log(`viewTemplateSetting`, this.router.getCurrentNavigation(), this.router.url);

    this.router.navigate([Urls.template], {
      queryParams: {
        thread: thread,
        lastUrl: this.router.url
      }
    })
  }

  private pathValue(data: Partial<Ticket>, options?: { onlySelf?: boolean, emitEvent?: boolean }) {
    console.log('pathValue', data, options)
    this.form.patchValue(data, options);
  }

  private loadMemberOfTeam(data: ClsTeam, stop: boolean = false): Observable<cls.ClsAssign[]> {
    if (notEmpty(data.members)) return of(data.members);
    else if (notEmpty(data.team_members)) return this.catalogSrv.searchAssignByIds(data.team_members);
    else if (stop === false) return this.loadMemberOfTeam(this.catalog.ls_helpdesk_team.find(t => t.id === data.id), true);
    else return of([]);

  }

  private templateToTicket(data: TicketTemplateData): Observable<Ticket> {
    data = Asserts.notNull(data, "TicketTemplateData is empty");
    const clsTeam = this.catalog.ls_helpdesk_team.find(s => data.team_id === s.id);

    return this.loadMemberOfTeam(clsTeam, false).pipe(map((members: cls.ClsAssign[]) => {
      const software = this.catalog.ls_software.find(s => data.software_id === s.id);
      const chanels = this.catalog.ls_chanel.filter(c => data.chanel_ids?.includes(c.id));
      const support_help = chanels?.find(c => data.support_help_id === c.id) ?? chanels[0];
      const assign = members?.find(s => data.assign_id === s.id);
      const emailTemplate = this.catalog.get_email().find(e => e.template_id === data.email_template_id);

      const now = new Date().getTime();

      this.lsSoftName = software?.soft_names ?? [];
      this.catalog.ls_assign = members;
      this.catalog.ls_team_head = [clsTeam?.team_head];

      return Ticket.from({
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

        email_template: emailTemplate,
        app_id: this.userLogin.ts_app,

        reception_time: this.utils.dateTo(now, 'yyyyMMdd-HH:mm'),
        complete_time: this.utils.dateTo(now, 'yyyyMMdd-HH:'),

        ticket_on: this.utils.dateTo(now, 'yyMMdd'),
        full_name: this.userLogin.ts_name

      });

    }))

  }

  signout(): void {
    const signinUrl = Urls.signinUrl;
    this.clearCache();
    this.authSrv.signout().subscribe({
      error: _ => this.router.navigate([signinUrl]),
      next: _ => this.router.navigate([signinUrl])
    })
  }


  //======================

  openEmailTemplate(): void {
    
    if (this.utils.isPreviewEmailHtml === false) {
      return;
    }

    if (this.utils.cEmailObject.invalid) {
      this.toast.warning(`Vui lòng nhập thông tin <b>[Email Ticket]</b>`);
    }
    else {
      const { email_template, email_object } = this.utils.formData;
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
  }

  set_data(data: AssignObject<Ticket>, template: TicketTemplate, submit?: boolean) {
    const defaultData = this.templateToTicket(template.data);
    this.formData = Objects.mergeDeep(defaultData, data);
    if (submit == true) this.saveTicket();
  }



  openDialogImportXsl(): void {
    const catalogs = this.catalog.get_ticket().filter(c => c.emailTicket);

    const defaultCatalog = isTrue(this.ticketTemplate?.emailTicket) ? this.ticketTemplate
      : catalogs.find(c => c.is_default) || catalogs[0];

    console.log(catalogs, defaultCatalog)

    const ref = openXslTemplate(this.modal, {
      file: {
        title: 'Tệp mẫu danh sách lỗi',
        name: 'ticket_dsloi.xlsx',
        link: '/assets/ticket_dsloi.xlsx',
        sheets: ['data'], editSheet: true,
      },
      fields: [
        {
          name: 'template',
          label: 'Dữ liệu mặc định',
          type: 'combo',
          required: true,
          value: defaultCatalog,
          class: 'p-fluid col-6',
          options: {
            items: catalogs,
            optionLabel: 'title',
            dataKey: 'template_id'
          }
        },
        {
          name: 'minute',
          label: 'Số phút [2-60]',
          class: 'p-fluid col-3',
          type: 'number',
          required: true, value: 10,
          options: { min: 2, max: 60 }
        }
      ],
      //validateXsl: wb => false

    });

    //
    ref.onClose.subscribe((rt: ReturnData) => {
      if (notNull(rt)) {
        this.state.visibleToolImport = true;
        const { minute } = rt.form_value;
        const dsItems: DsLoiItem[] = rt.sheets['data'].rows;
        const template: TicketTemplate = rt.form_value['template'];

        const allObser = this.templateToTicket(template.data).pipe(map(ticket => {
          const rows: Ticket[] = [];

          for (let item of dsItems) {
            let newt = ticket.clone();
            newt.tax_code = item.masothue;
            newt.phone = item.dienthoai;
            newt.email = item.email;
            newt.subject = item.tenhoso + ` báo lỗi ngày ${this.utils.dateTo(new Date(), 'dd/MM/yyyy')}`;
            newt.content_help = item.motaloigiaodich;
            newt.complete_time = this.utils.dateTo(Date.now() + minute * 60_000, 'yyyyMMdd-HH:mm'); // + 10p
            //newt.options.emailTicket = true;

            if (notNull(newt.email_template)) {
              const fields = newt.email_template.data.fields || [];
              newt.email_object = Objects.arrayToJson(fields, (f: any) => [f.name, item[f.name]]);
            }

            rows.push(newt);
          }


          return rows;
        }));

        allObser.subscribe({
          next: rows => {
            this.idata = new ImportData(this, rows);
            this.idata.clickNext();
            //this.idata.isClickRunOne = null;
          }
        });


      }
    });


  }

  tagSelectClass(item: any): string {
    let hasChecked = this.ticketTemplate?.template_id === item.template_id;
    return hasChecked ? 'select' : undefined;
  }

  clickViewAll(checked: boolean): void {}

  formCls(): any {
    return {
      [`template`]: this.viewTemplate,
      [`is-email-ticket`]: this.utils.is_email_ticket
    }
  }

}