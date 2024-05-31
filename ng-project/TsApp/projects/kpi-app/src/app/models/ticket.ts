import { Objects } from "../utils/objects";
import { BaseModel } from "./base-model";
import { Chanel } from "./chanel";
import { ImageObject, JsonObject } from "./common";
import { GroupHelp } from "./group-help";
import { Question } from "./question";
import { Software } from "./software";
import { TicketDetail } from "./ticket-detail";
import { TicketOption } from "./ticket-option";
import * as cls from "./od-cls";

export interface TemplateObj  {
  code?: string;
  title?: string;
  icon?: string;
  summary?: string;
  bg_color?: string;
  text_color?: string;
}

export enum TicketStatus {
  NEW,
  PROCESSING,
  COMPLETE,
  CLOSED,
  CANCEL,
  OPENING
}

export class TemplateMap extends Map<string, Ticket> {

  save(data: Ticket) {
    const title = data.template.title;
    this.set(title, data);
  }

  get listTitle(): string[] {
    return [...this.keys()];
  }

  style(title: string): any {
    const temp: TemplateObj = this.get(title)?.template ?? {};
    return {
      'background-color': temp.bg_color,
      'color': temp.text_color
    };
  }
}

export class Ticket extends BaseModel {
  ticket_id?: number;
  full_name?: string;
  tax_code?: string;
  company_name?: string;
  phone?: string;
  teamviewer?: string;
  customer_name?: string;
  content_required?: string;
  content_help?: string;
  reception_time?: string;
  complete_time?: string;
  content_copy?: string;
  email?: string;
  subject?: string;
  body?: string;
  note?: string;
  reply?: string;
  content_email?: string;
  group_help?: GroupHelp;
  question: Question;
  software?: Software;
  chanels?: Chanel[];
  support_help?: Chanel;
  soft_name?: string;
  source?: string;
  client_version?: string;
  ticket_status?: TicketStatus;
  user_id?: number;
  template?: TemplateObj;
  options?: TicketOption;
  details?: TicketDetail;
  od_image?: ImageObject;
  od_assign?: cls.ClsAssign;
  od_category_sub?: cls.ClsCategorySub;
  od_category?: cls.ClsCategory;
  od_partner?: cls.ClsPartner;
  od_priority?: cls.ClsPriority;
  od_repiled?: cls.ClsRepiled;
  od_subject_type?: cls.ClsSubjectType;
  od_tags?: cls.ClsTags;
  od_team?: cls.ClsTeam;
  od_team_head?: cls.ClsTeamHead;
  od_ticket_type?: cls.ClsTicketType;
  od_topic?: cls.ClsTopic;

  edit_ticket?: boolean;
  edit_note?: boolean;
  edit_reply?: boolean;
  is_web?: boolean;
  is_delete?: boolean;
  is_report?: boolean;

  constructor() {
    super();
  }

  static from(data: JsonObject): Ticket {
    return new Ticket().update(data);
  }

  override update(object: JsonObject, init?: boolean): this {
    const ticket = super.update(object);
    Objects.ifNotNull(object['options'], val => ticket.options = TicketOption.from(val));
    Objects.ifNotNull(object['group_help'], val => ticket.group_help = GroupHelp.from(val));
    Objects.ifNotNull(object['question'], val => ticket.question = Question.from(val));
    Objects.ifNotNull(object['software'], val => ticket.software = Software.from(val));
    Objects.ifNotNull(object['support_help'], val => ticket.support_help = Chanel.from(val));
    Objects.ifListNotEmpty(object['chanels'], val => ticket.chanels = Chanel.fromList(val));
    return ticket;
  }

  get_options(): TicketOption {
    if(Objects.isEmpty(this.options)) {
      this.options = TicketOption.createDef();
    }
    return  this.options;
  }

}
