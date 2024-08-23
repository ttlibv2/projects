import {TicketFormComponent} from "./ticket-form.component";
import {Asserts, ControlKey, Forms, Objects, ValueOption} from "ts-ui/helper";
import {Ticket} from "../../models/ticket";
import {FormBuilder, Validators} from "@angular/forms";
const {isBlank,  isNull, notBlank} = Objects;

export class TicketFormGroup {
  private forms: Forms<Ticket>;
  private get options() { return this.comp.options;}
  private get isAutoCreate() { return this.options.autoCreate === true;}
  private get user() { return this.comp.userLogin;}

  get formValue(){return this.forms?.formRawValue;}
  get formGroup() { return this.forms.formGroup; }
  get invalid() { return this.formGroup.invalid; }

  constructor(private readonly comp:TicketFormComponent,
              private readonly fb: FormBuilder) {
  }

  initialize(): Forms<Ticket> {
    Asserts.isNull(this.forms, "The ticket form has initialize");

    return this.forms = Forms.builder<Ticket>(this.fb, {
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
      email_template: [null],
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
      od_partner_id: [{value: null, disabled: true}],
      od_priority: [null],
      od_replied: [null, Validators.required],
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
      email_object: this.fb.group({}),
      edit_note: [true],
      edit_ticket: [true]
    });
  }

  registerListener(): void {
    const forms: Forms<Ticket> = this.forms;

    forms.controlsValueChange(['options'], (forms, json) => this.options.update(json));

    forms.controlsValueChange(['tax_code', 'support_help', 'ticket_on'], (forms, json) => {
      if(this.isAutoCreate) forms.pathValueControl('subject', this.createSubject());
    });

    forms.controlsValueChange(['support_help', 'complete_time', 'group_help', 'content_help'], (forms, json) => {
      if(this.isAutoCreate)forms.pathValueControl('note', this.createNote());
    });

    forms.controlsValueChange(['phone', 'content_required', 'soft_name', 'support_help', 'reception_time'], (forms, json) => {
      if(this.isAutoCreate) forms.pathValueControl('body', this.createBody());
    });

    forms.controlsValueChange(['subject', 'body', 'note', 'email', 'tax_code', 'customer_name'], (forms, json) => {
      if(this.isAutoCreate) forms.pathValueControl('content_copy', this.createContentCopy());
    });

    forms.controlsValueChange(['od_partner_id'], (forms, json) => {
      if(isBlank(json['od_partner_id'])) forms.pathValueControl('od_partner', undefined);
    });
  }

  resetForm(value?: Partial<Ticket>, options?: ValueOption) {
    this.forms.resetForm(value, options);
  }

  pathValue(value?: Partial<Ticket>, options?: ValueOption) {
    this.forms.pathValue(value, options);
  }

  createSubject(): string {
    const {tax_code, support_help, ticket_on} = this.formValue;
    const {room_code, user_code} = this.user;
    if (isBlank(tax_code)) return undefined;
    else return `[${room_code}-${support_help.code}]-${user_code}-${tax_code}-${ticket_on}`;
  }

  createNote(): string {
    const {complete_time, group_help, support_help, content_help, full_name} = this.formValue;

    const lines: string[] = [];


    lines.push(`- Họ tên: ${full_name}`);

    if (!isNull(support_help)) {
      lines.push(`- Hình thức hỗ trợ: ${support_help["value"]}`);
    }

    if ( !isNull(group_help)) {
      lines.push(`- Nhóm hỗ trợ: ${group_help.title}`);
    }

    if (notBlank(content_help)) {
      lines.push(`- Nội dung đã hỗ trợ: ${content_help} `);
    }

    if (notBlank(complete_time)) {
      lines.push(`- Thời gian hoàn tất: ${complete_time}`);
    }

    if (lines.length > 1) {
      this.forms.pathValue({edit_ticket: true, edit_note: true});
    }

    return lines.join('\n');
  }

  createBody(): string {
    const lines: string[] = [];
    const {phone, content_required, soft_name, support_help, reception_time, full_name} = this.formValue;

    if (notBlank(phone)) {
      lines.push(`- Số điện thoại liên hệ: ${phone}`);
    }

    if ( notBlank(content_required)) {
      let soft_name2 = notBlank(soft_name) ? `${soft_name} - ` : "";
      let support = !isNull(support_help)  ? ` - ${support_help.support}`  : "";
      lines.push(`- Nội dung cần hỗ trợ: ${soft_name2}${content_required}${support}`);
    }

    lines.push(`- Nhân viên gửi thông tin: ${full_name}`);


    if ( notBlank(reception_time)) {
      lines.push(`- Thời gian tiếp nhận: ${reception_time}`);
    }

    if (lines.length > 1) {
      this.forms.pathValue({edit_ticket: true});
    }

    return lines.join('\n');
  }

  /**
   * Create Content copy
   * @param obj {[key: string]: any}
   * @param createIfExist boolean
   * */
  createContentCopy(options?: {createIfExist?: boolean, subject?: string, body?: string, note?: string}): string {
    //createIfExist = createIfExist || false;
    options = Object.assign({}, options);

    const notes: string[] = [];
    const {email, tax_code, customer_name, body, note} = this.formValue;

    // email
    if (notBlank(email))  notes.push(`${email}`);

    //-- subject email
    const subject = options.subject ?? this.createSubject();
    if (notBlank(subject)) notes.push(subject);

    if (notBlank(tax_code)) {
      notes.push(`- Mã số thuế / Mã đơn vị: ${tax_code}`);
    }

    if (notBlank(customer_name)) {
      notes.push(`- Tên khách hàng: ${customer_name}`);
    }

    if (notBlank(email)) {
      notes.push(`- Email: ${email}`);
    }

    const bodyFnc = () => this.createBody();
    const noteFnc = () => this.createNote();

    notes.push(options.createIfExist ? bodyFnc() : options.body || body || bodyFnc());
    notes.push(`--------------------------------------------`);
    notes.push(options.createIfExist ? noteFnc() : options.note || note || noteFnc());

    return notes.join(`\n`);
  }

  copyValue() {
    this.resetForm({ ...this.formValue, ticket_id: undefined });
  }

  pathControlValue<C extends ControlKey<Ticket>>(name: C, value: Ticket[C]) {
    return this.forms.pathValueControl(name, value);
  }
}