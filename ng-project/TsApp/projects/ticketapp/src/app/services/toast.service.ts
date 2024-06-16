import {Injectable} from "@angular/core";
import { MessageService} from "primeng/api";
import {MessageObj, Severity} from "../models/common";
import {Objects} from "../utils/objects"

const {isEmpty, isBlank} = Objects;

@Injectable({ providedIn: 'root' })
export class ToastService {

  constructor(private message: MessageService) {
  }

  private buildDetail(message: MessageObj): string {
    if(isEmpty(message.details)) return message.detail;
    else return '- ' + message.details.join('<br>- ');
  }

  private buildSummary(message: MessageObj): string {
    const code = '';//isBlank(message.code) ? '' : ` (<b>Mã code: <i>${message.code}</i></b>)`;
    return `${message.summary}${code}`;
  }

  show(severity: Severity, message: MessageObj): void {
    const detail = this.buildDetail(message);
    const summary = this.buildSummary(message);
    message.life = message.life ?? 60 * 60 * 1000; // 1h
    this.message.add({...message,
      severity, detail, summary,
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

  info(message: MessageObj): void {
    this.show('info', message);
  }

  loading(message: MessageObj): void {
    this.show('error', {...message, 
      life: 60 * 60 * 1000,
      icon: 'pi pi-spin pi-spinner'
    })
  }

  clearAll(): void {
    this.message.clear('TOAST_MESSAGE');
  }
}
