import { AssignObject } from "ts-ui/helper";
import {BaseModel} from "./base-model";

export class TicketLog extends BaseModel {

  static from(json: AssignObject<TicketLog>): TicketLog {
    return BaseModel.fromJson(TicketLog, json);
  }
  
}
