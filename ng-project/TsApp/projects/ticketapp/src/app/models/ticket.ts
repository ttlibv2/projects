import { Objects } from "ts-helper";
import { BaseModel } from "./base-model";
import { Chanel } from "./chanel";
import { AssignObject, ImageObject } from "./common";
import { GroupHelp } from "./group-help";
import { Question } from "./question";
import { Software } from "./software";
import { TicketDetail } from "./ticket-detail";
import { TicketOption } from "./ticket-option";
import * as cls from "./od-cls";



export enum TicketStatus {
  NEW,
  PROCESSING,
  COMPLETE,
  CLOSED,
  CANCEL,
  OPENING
}

export interface TicketTemplateData {
  software_id?: number;
  chanel_ids?: number[];
  group_help_id?: number;
  support_help_id?: number;
  soft_name?: string;
  team_id?: number;
  assign_id?: number;
  subject_type_id?: number;
  category_id?: number;
  category_sub_id?: number;
  team_head_id?: number;
  priority_id?: number;
  tag_ids?: number[];
  repiled_id?: number;
  ticket_type_id?: number;
  options?: {
    [key: keyof TicketOption]: any
  }

}





export class Ticket extends BaseModel {
  ticket_id: number;
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
  options: TicketOption = TicketOption.createDef();
  details?: TicketDetail;
  od_image?: ImageObject;
  od_assign?: cls.ClsAssign;
  od_category_sub?: cls.ClsCategorySub;
  od_category?: cls.ClsCategory;
  od_partner?: cls.ClsPartner;
  od_priority?: cls.ClsPriority;
  od_repiled?: cls.ClsRepiled;
  od_subject_type?: cls.ClsSubjectType;
  od_tags?: cls.ClsTag[];
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

  ticket_on?: string;

  constructor() {
    super();
  }

  static from(data: AssignObject<Ticket>): Ticket {
    return BaseModel.fromJson(Ticket, data);
  }

  override update(object: AssignObject<Ticket>): this {

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