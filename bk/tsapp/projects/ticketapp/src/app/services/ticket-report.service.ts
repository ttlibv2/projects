import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {ResponseToModel} from "../models/common";
import {TicketReport} from "../models/ticket-report";

@Injectable({providedIn: 'root'})
export class TicketReportService  extends ModelApi<TicketReport> {

  override basePath(): string {
    return `/ts-api/ticket/report`;
  }

  override resToModel(): ResponseToModel<any> {
    return json => TicketReport.from(json);
  }


}
