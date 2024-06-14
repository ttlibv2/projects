import {BaseModel} from "./base-model";
import {JsonObject} from "./common";

export class TicketReport extends BaseModel {

  static from(json: JsonObject): TicketReport {
    return new TicketReport().update(json);
  }

}
