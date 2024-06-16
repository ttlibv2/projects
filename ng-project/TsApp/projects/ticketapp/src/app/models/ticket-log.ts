import {BaseModel} from "./base-model";
import {JsonObject} from "./common";

export class TicketLog extends BaseModel {

  static from(json: JsonObject): TicketLog {
    return new TicketLog().update(json);
  }
}
