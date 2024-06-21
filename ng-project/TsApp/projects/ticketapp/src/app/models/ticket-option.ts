import { BaseModel } from "./base-model";
import { JsonObject } from "./common";

export class TicketOption extends BaseModel {
    autoCreate?: boolean;
    autoFill?: boolean;
    viewAll?: boolean;
    viewTs24?: boolean;
    saveCache?: boolean;
    emailTicket?: boolean;

    static from(data: JsonObject): TicketOption {
      return new TicketOption().update(data);
    }

    static createDef(): TicketOption {
      return TicketOption.from({
        autoCreate: true,
        autoFill: true,
        viewAll: true,
        viewTs24: false,
        saveCache: false,
        emailTicket: false,
      });
    }

  }
