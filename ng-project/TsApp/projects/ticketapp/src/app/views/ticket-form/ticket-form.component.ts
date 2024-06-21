import { ChangeDetectionStrategy, Component, Input, OnInit, ViewEncapsulation, booleanAttribute } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { MenuItem } from "primeng/api";
import { Ticket } from "../../models/ticket";
import { CheckboxChangeEvent } from "primeng/checkbox";
import { DialogService } from "primeng/dynamicdialog";
import { TemplateFormComponent } from "../template-form/template-form.component";
import { TicketOption } from "../../models/ticket-option";
import { FindPartnerComponent } from "../find-partner/find-partner.component";
import { CatalogService } from "../../services/catalog.service";

import { Catalog } from "../../models/catalog";
import { ToastService } from "../../services/toast.service";
import { Objects, Optional } from "ts-helper";
import { Software } from "../../models/software";
import { ClsTeam } from "../../models/od-cls";
import { ConfigService } from "../../services/config.service";
import { LoggerService } from "ts-logger";
import { of } from "rxjs";
import { Chanel } from "../../models/chanel";
import { Template } from "../../models/template";
import { TemplateService } from "../../services/template.service";

const { notNull, notBlank, notEmpty } = Objects;

export type KEYS = "clsHelpdeskTeam";
export interface Loading {
  assign?: boolean;
}

export interface OptionLabel {
  label?: string;
  hidden?: boolean;
  formControl?: string;
  command?: (event: any) => void;
}

@Component({
  selector: "ts-ticket-form",
  templateUrl: "./ticket-form.component.html",
  styleUrl: "./ticket-form.component.scss",
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TicketFormComponent implements OnInit {
  loading: Loading = {};

  ticketForm: FormGroup;
  ticket: Ticket = new Ticket();
  asyncLoadCate: boolean = false;
  

  catalog: Catalog = new Catalog();
  template: Template;

  lsSoftName: string[] = [];

  toolActions: MenuItem[] = [
    {
      label: "Tải danh mục",
      icon: "pi pi-home",
      command: (event) => this.loadCatalogs(),
    }
  ];

  ticketOptions: OptionLabel[] = [
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
    },
    {
      label: "Hiển thị mẫu",
      formControl: "viewTemplate",
      hidden: false,
      command: (checked) => {
        this.viewTemplate = checked;
        this.template = Template.from({
          title: 'Mẫu mặc định',

        });
      },
    },
  ];

  get isEditTicket(): boolean {
    return false;
  }

  get isEmailTicket(): boolean {
    return this.options.emailTicket ?? false;
  }

  get isViewAll(): boolean {
    return this.options.viewAll ?? true;
  }

  get isViewTs24(): boolean {
    return this.options.viewTs24 ?? false;
  }

  get options(): TicketOption {
    return this.ticket.get_options();
  }

  get formRawValue(): Ticket {
    return this.ticketForm.getRawValue();
  }

  @Input()
  set data(ticket: Ticket) {}

  @Input({transform: booleanAttribute})
  viewTemplate: boolean = false;

  constructor(
    private fb: FormBuilder,
    private toast: ToastService,
    private config: ConfigService,
    private templateSrv: TemplateService,
    private catalogSrv: CatalogService,
    private logger: LoggerService ) {
      this.createFormGroup();
    }

  ngOnInit() {
    this.loadCatalogs();
    this.ticketForm.patchValue(this.ticket);
  }

  private createFormGroup(): void {
    this.ticketForm = this.fb.group({
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
      group_help: [null],
      question: [null],
      software: [null],
      chanels: [null],
      support_help: [null],
      soft_name: [null],
      od_image: [null],
      od_assign: [null],
      od_category_sub: [null],
      od_category: [null],
      od_partner: [null],
      od_partner_id: null,
      od_priority: [null],
      od_repiled: [null],
      save_question: [false],
      od_subject_type: [null],
      od_tags: [null],
      od_team: [null],
      od_team_head: [{ value: null, disabled: true }],
      od_ticket_type: [null],
      od_topic: [null],
      options: this.fb.group({
        autoCreate: [null],
        autoFill: [null],
        viewAll: [null],
        viewTs24: [null],
        saveCache: [null],
        emailTicket: [null],
        viewTemplate: [this.viewTemplate],
      }),
    });
  }

  loadCatalogs() {
    this.asyncLoadCate = true;
    
    const ref = this.toast.loading({
      summary: "Đang lấy danh mục. Vui lòng chờ",
    });

    this.catalogSrv.getAll().subscribe({
      error: (err) => {
        this.asyncLoadCate = false;
        this.toast.close(ref);
        this.logger.warn("load catalog error => ", err);
      },
      next: (res) => {
        this.catalog = res;
        this.logger.info("load catalog success => ", res);
        this.toast.close(ref);
        this.toast.success({ summary: this.config.i18n.loadCatalogOk });
      },
    });
  }

  selectTemplate(title: string) {
  }

  saveTicket() {
    const formValue = this.ticketForm.getRawValue();

    if(this.viewTemplate) {
      const data = Objects.extractValueNotNull(formValue);
    }

    console.log(this.ticketForm.getRawValue());
  }

  searchUser() {
    const { tax_code, phone, email } = this.formRawValue;
    this.toast.openDialog(FindPartnerComponent, {
      header: "Tìm kiếm khách hàng",
      data: {
        vat: tax_code,
        mobile: phone,
        email: email,
      },
    });
  }

  changeSelectedItem(field: string, data: any) {
    if ("#software" === field) {
      const item: Software = data;
      this.lsSoftName = notNull(data) ? item.soft_names : [];
      this.pathValue("soft_name", this.lsSoftName[0]);
    } else throw new Error("Method not implemented.");
  }

  onDropdownSearch(field: string, data: any) {
    this.catalogSrv.searchAssign(data);
  }

  onDropdownChange(field: KEYS, data: any) {
    this.logger.info(field, ": ", data);

    if (field === "clsHelpdeskTeam") {
      this.onSelectHelpdeskTeam(data);
    } else throw new Error("Method not implemented.");
  }

  onSelectHelpdeskTeam(data: ClsTeam) {
    Optional.ofNullable(data.members)
      .map((users) => of(users))
      .orElseGet(() => this.catalogSrv.searchAssignByIds(data.team_members))
      .subscribe({
        error: (err) => (this.loading.assign = false),
        next: (members) => {
          this.loading.assign = false;
          data.members = members;
          this.catalog.ls_assign = members;
          this.catalog.team_head = data?.team_head;
          this.pathValue("od_team_head", data?.team_head);
        },
      });
  }

  onSelectChanel(data: Chanel[]): void {
    this.pathValue("support_help", data ? data[0] : undefined);
  }

  private pathValue(path: string, value: any) {
    this.ticketForm.patchValue({ [path]: value });
  }
}
