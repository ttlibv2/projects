import {Injectable} from "@angular/core";
import { MessageService} from "primeng/api";
import {MessageObj, Severity} from "../models/common";
import {Objects} from "../utils/objects"
import {Toast, ToastrService} from "ngx-toastr";

const {isEmpty, isBlank, isArray, isObject} = Objects;

@Injectable({ providedIn: 'root' })
export class ToastService {

  constructor(//public message: MessageService,
              public message: ToastrService) {
  }

  private buildDetail(message: MessageObj): string {
    const md: any = message.details;
    if(isEmpty(md) || isObject(md) || !isArray(md)) return "";
    else return '- '+ md.join('<br>- ');
  }

  private buildSummary(message: MessageObj): string {
    const code = '';//isBlank(message.code) ? '' : ` (<b>Mã code: <i>${message.code}</i></b>)`;
    return `${message.summary}${code}`;
  }

  show(severity: Severity, message: MessageObj) {
    //const detail = this.buildDetail(message);
    //const summary = this.buildSummary(message);
    //message.life = message.life ?? 60 * 60 * 1000; // 1h
    //this.message.add({...message,
    //  severity, detail, summary,
    //  //key: 'TOAST_MESSAGE'
    //});

    return this.message.show(severity, message.summary);
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
    this.show('primary', {...message,
      life: 60 * 60 * 1000,
      icon: 'pi pi-spin pi-spinner'
    })
  }

  clearAll(): void {
    //this.message.clear('TOAST_MESSAGE');
  }
}
