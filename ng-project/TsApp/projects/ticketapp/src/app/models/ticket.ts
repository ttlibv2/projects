import { Objects, AssignObject } from "ts-ui/helper";
import { BaseModel } from "./base-model";
import { Chanel } from "./chanel";
import { ImageObject } from "./common";
import { GroupHelp } from "./group-help";
import { Question } from "./question";
import { Software } from "./software";
import { TicketOption } from "./ticket-option";
import * as cls from "./od-cls";
import { EmailTemplate, TicketTemplate} from "./template";

const {isNull, notNull} = Objects;

export enum TicketStatus {
  NEW,
  PROCESSING,
  COMPLETE,
  CLOSED,
  CANCEL,
  OPENING
}


export class OdTicketSend extends BaseModel {
  ticket_id: number;
  action: string;
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
  question?: Question;
  software?: Software;
  chanels?: Chanel[];
  support_help?: Chanel;
  soft_name?: string;
  source?: string;
  client_version?: string;
  ticket_status?: TicketStatus;
  user_id?: number;
  chanel_ids?: number[];
  template_id?: number;
  email_template?: EmailTemplate;
  ticket_template?: TicketTemplate;
  images: string;
  options?: TicketOption = TicketOption.createDef();
  od_image?: ImageObject;
  od_assign?: cls.ClsAssign;
  od_category_sub?: cls.ClsCategorySub;
  od_category?: cls.ClsCategory;
  od_partner?: cls.ClsPartner;
  od_priority?: cls.ClsPriority;
  od_replied?: cls.ClsReplied;
  od_subject_type?: cls.ClsSubjectType;
  od_tags?: cls.ClsTag[];
  od_team?: cls.ClsTeam;
  od_team_head?: cls.ClsTeamHead;
  od_ticket_type?: cls.ClsTicketType;
  od_topic?: cls.ClsTopic;

  od_partner_id?: number;

  edit_ticket?: boolean;
  edit_note?: boolean;
  edit_reply?: boolean;
  is_web?: boolean;
  is_delete?: boolean;
  is_report?: boolean;

  ticket_on?: string;

  //----
  download_url?: string;
  view_url?: string;
  form_url?: string;
  ticket_text?: string;
  ticket_number?: number;
  note_id?: number;
  reply_id?: number;
  cancel_reason?: string;
  stage_text?: string;
  stageId?: number;
  closeBy?: number;
  mail_id?: number;

  reply_at?: string;
  ticket_at?: string;
  note_at?: string;
  close_at?: string;
  delete_at?: string;
  image_at?: string;
  cancel_at?: string;
  mail_at?: string;


  send_status?: 'loading' | 'success' | 'error' | undefined;
  view_chanel?: boolean = false;


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


    if('od_partner' in object) {
      this.od_partner_id = object.od_partner.id;
    }

    return this;
  }


  get_options(): TicketOption {
    if(Objects.isEmpty(this.options)) {
      this.options = TicketOption.createDef();
    }
    return  this.options;
  }

  cloneWithChanel(): Ticket[] {
    const object = Objects.assign({}, this);
    return this.chanels.map(chanel => Ticket.from({...object, support_help: chanel, view_chanel: true}));
  }

  hasSend(): boolean {
    return notNull(this.ticket_number);
  }

}