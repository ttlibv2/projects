import { FormGroup } from "@angular/forms";
import { Ticket } from "../../models/ticket";
import { User } from "../../models/user";
import { Objects } from "ts-helper";

const { isBlank, isEmpty, isNull, notBlank, notNull } = Objects;

function dirty(fg: FormGroup, key: string): boolean {
  if (isNull(fg)) return true;
  if (isNull(fg.get(key))) return false;
  else return fg.get(key).dirty;
  //else return fg.get(key)['_pendingDirty'];
}

export class TicketUtil {
  static createSubject(user: User, obj: Ticket): string {
    if (isBlank(obj.tax_code) || isEmpty(obj.chanels)) return undefined;
    else
      return `[${user.room_code}-${obj.support_help.code}]-${user.user_code}-${obj.tax_code}-${obj.ticket_on}`;
  }

  static createNote(obj: Ticket, fg: FormGroup): string[] {
    const lines: string[] = [];

    if (dirty(fg, "full_name") && notBlank(obj.full_name)) {
      lines.push(`- Họ tên: ${obj.full_name}`);
    }

    if (dirty(fg, "support_help") && !isNull(obj.support_help)) {
      lines.push(`- Hình thức hỗ trợ: ${obj.support_help["value"]}`);
    }

    if (dirty(fg, "group_help") && !isNull(obj.group_help)) {
      lines.push(`- Nhóm hỗ trợ: ${obj.group_help.title}`);
    }

    if (dirty(fg, "content_help") && notBlank(obj.content_help)) {
      lines.push(`- Nội dung đã hỗ trợ: ${obj.content_help} `);
    }

    if (dirty(fg, "complete_time") && notBlank(obj.complete_time)) {
      lines.push(`- Thời gian hoàn tất: ${obj.complete_time}`);
    }

    if (lines.length > 0) {
      obj.edit_ticket = true;
      obj.edit_note = true;
    }

    return lines;
  }

  static createBody(obj: Ticket, fg: FormGroup) {
    const lines: string[] = [];

    if (dirty(fg, "phone") && notBlank(obj.phone)) {
      lines.push(`- Số điện thoại liên hệ: ${obj.phone}`);
    }

    if (dirty(fg, "content_required") && notBlank(obj.content_required)) {
      let body = obj.content_required;
      let soft_name = notBlank(obj.soft_name) ? `${obj.soft_name} - ` : "";
      let support = !isNull(obj.support_help)
        ? ` - ${obj.support_help.support}`
        : "";
      lines.push(`- Nội dung cần hỗ trợ: ${soft_name}${body}${support}`);
    }

    if (dirty(fg, "full_name") && notBlank(obj.full_name)) {
      lines.push(`- Nhân viên gửi thông tin: ${obj.full_name}`);
    }

    if (dirty(fg, "reception_time") && notBlank(obj.reception_time)) {
      lines.push(`- Thời gian tiếp nhận: ${obj.reception_time}`);
    }

    if (lines.length > 0) {
      obj.edit_ticket = true;
    }

    return lines;
  }

  /**
   * Create Content copy
   * @param obj {[key: string]: any}
   * @param createIfExist boolean
   * */
  static createContentCopy(
    user: User,
    obj: Ticket,
    fg: FormGroup,
    createIfExist: boolean = false,
    options: any = {}
  ): string {
    createIfExist = createIfExist || false;
    options = Object.assign({}, options);

    const notes: string[] = [];

    // email
    if (notBlank(obj.email)) {
      notes.push(`${obj.email}`);
    }

    //-- subject email
    const subject = options.subject || TicketUtil.createSubject(user, obj);
    if (notNull(subject)) {
      notes.push(subject);
    }

    if (notBlank(obj.tax_code)) {
      notes.push(`- Mã số thuế / Mã đơn vị: ${obj.tax_code}`);
    }

    if (notBlank(obj.customer_name)) {
      notes.push(`- Tên khách hàng: ${obj.customer_name}`);
    }

    if (notBlank(obj.email)) {
      notes.push(`- Email: ${obj.email}`);
    }

    const body = () => TicketUtil.createBody(obj, fg).join("<br>");
    const note = () => TicketUtil.createNote(obj, fg).join("<br>");

    notes.push(createIfExist ? body() : options.body || obj.body || body());
    notes.push(`--------------------------------------------`);
    notes.push(createIfExist ? note() : options.note || obj.note || note());

    return notes.join(`\n`);
  }
}
