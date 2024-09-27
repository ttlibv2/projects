import { AssignObject } from "ts-ui/helper";
import {BaseModel} from "./base-model";

export class TicketReport extends BaseModel {

  static from(json: AssignObject<TicketReport>): TicketReport {
    return BaseModel.fromJson(TicketReport, json);
  }

}
