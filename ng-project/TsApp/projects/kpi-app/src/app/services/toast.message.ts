import {Injectable} from "@angular/core";
import {Message, MessageService} from "primeng/api";
import {MessageObj, Severity} from "../models/common";
import {Objects} from "../utils/objects";
import {I18nService} from "./i18n.service";
const {isEmpty, isBlank} = Objects;

@Injectable({ providedIn: 'root' })
export class ToastService {

  constructor(private message: MessageService,
              private i18n: I18nService) {
  }

  private buildDetail(message: MessageObj): string {
    if(isEmpty(message.details)) return message.detail;
    else return '- ' + message.details.join('<br>- ');
  }

  private buildSummary(message: MessageObj): string {
    const code = isBlank(message.code) ? '' : ` (<b>Mã code: <i>${message.code}</i></b>)`;
    return `${message.summary}${code}`;
  }

  show(severity: Severity, message: MessageObj): void {
    const detail = this.buildDetail(message);
    const summary = this.buildSummary(message);
    this.message.add({...message,
      severity, detail, summary,
      life: 3000,
      key: 'TOAST_MESSAGE'
    });
  }

  warning(message: MessageObj): void {
    this.show('warn', message);
  }

  error(message: MessageObj): void {
    this.show('error', message);
  }

  success(message: MessageObj): void {
    this.show('success', message);
  }



}
