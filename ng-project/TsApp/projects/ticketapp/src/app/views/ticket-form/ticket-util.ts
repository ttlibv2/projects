import { FormGroup } from "@angular/forms";
import { Ticket } from "../../models/ticket";
import { User } from "../../models/user";
import { Consumer, Objects } from "ts-helper";
import { TicketFormComponent } from "./ticket-form.component";
import { merge, mergeAll, mergeMap, of, scheduled } from "rxjs";
import { Mode } from "fs";
import { TicketOption } from "../../models/ticket-option";

const { isBlank, isEmpty, isNull, notBlank, notNull } = Objects;

export const controlEditors: string[] = [];


export interface ValueOption {
  onlySelf?: boolean;
  emitEvent?: boolean
}

export class TicketUtil2 {


  get user(): User { return this.comp.userLogin; }
  get form(): FormGroup { return this.comp.ticketForm; }
  get formValue(): Ticket { return this.form.getRawValue(); }
  get options(): TicketOption { return this.comp.options; }
  get autoCreate(): boolean { return this.options.autoCreate; }

  constructor(public comp: TicketFormComponent) {}

  registerListener() {

    this.valueChangeFor(['tax_code', 'support_help'], json => {
      if(this.autoCreate === true)
        this.pathValueForm({subject: this.createSubject()});
    });

    this.valueChangeFor(['support_help', 'complete_time', 'group_help', 'content_help'], json => {
      if(this.autoCreate === true){
        this.pathValueForm({note: this.createNote()});
     }
    });
    
    this.valueChangeFor(['phone', 'content_required', 'soft_name', 'support_help', 'reception_time'], json => {
      if(this.autoCreate === true)
         this.pathValueForm({body: this.createBody()})
    });

    this.valueChangeFor(['subject', 'body', 'note', 'email', 'tax_code', 'customer_name'], json => {
      if(this.autoCreate === true) 
        this.pathValueForm({content_copy: this.createContentCopy()})
    });

    this.valueChangeFor(['options'], json => this.options.update(json));
    
    this.valueChangeFor(['od_partner_id'], json => {
      if(isBlank(json['od_partner_id'])) this.pathValueForm({od_partner: undefined});
    });

  }

  createSubject(): string {
    const {tax_code, chanels, support_help, ticket_on} = this.formValue;
    const {room_code, user_code} = this.user;
    if (isBlank(tax_code) || isEmpty(chanels)) return undefined;
    else return `[${room_code}-${support_help.code}]-${user_code}-${tax_code}-${ticket_on}`;
  }

  createNote(): string {
    const {complete_time, group_help, support_help, content_help, full_name} = this.formValue;

    const lines: string[] = [], fg = this.form;

  
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
      this.pathValueForm({edit_ticket: true, edit_note: true});
    }

    return lines.join('\n');
  }

  createBody(): string {
    const lines: string[] = [], fg = this.form;
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
      this.pathValueForm({edit_ticket: true})
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
    if (notNull(subject)) notes.push(subject);
    
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

  pathValueForm(data: Partial<Ticket>, options?: ValueOption) {
    this.form.patchValue(data, {onlySelf: false, emitEvent: true, ...options});
  }

  valueChangeFor(controls: (keyof Ticket)[], nextCb: Consumer<any>) {
   controls.forEach((c: any) => this.form.get(c).valueChanges.subscribe({
    next: res => nextCb({[c]: res})
   }))
  }

}




















