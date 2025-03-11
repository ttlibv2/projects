import { AssignObject } from "ts-ui/helper";
import { BaseModel } from "./base-model";

export class TicketDetail extends BaseModel {
    ticket_id?: number;
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
  
    static from(o: AssignObject<TicketDetail>): TicketDetail {
      return BaseModel.fromJson(TicketDetail, o);
    }
  
  }