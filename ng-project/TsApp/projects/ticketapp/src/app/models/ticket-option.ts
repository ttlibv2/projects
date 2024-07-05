import { BaseModel } from "./base-model";
import { JsonObject } from "./common";


export class TicketOption extends BaseModel {
    autoCreate: boolean = true;
    autoFill: boolean = true;
    viewAll: boolean = false;
    viewTs24: boolean = false;
    saveCache: boolean = false;
    emailTicket: boolean = false;

    static from(data: JsonObject): TicketOption {
      return BaseModel.fromJson(TicketOption, {...defaultOption, ...data});
    }

    static createDef(): TicketOption {
      return defaultOption;
    }

  }

  
export const defaultOption = TicketOption.from({
  autoCreate: true,
  autoFill: true,
  viewAll: true,
  viewTs24: false,
  saveCache: false,
  emailTicket: false,
});