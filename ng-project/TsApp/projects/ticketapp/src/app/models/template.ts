import { Asserts, TsMap } from "ts-ui/helper";
import { BaseModel } from "./base-model";
import { AssignObject, Severity } from "./common";

export type TemplateCode = 'form_ticket' | 'email_ticket';

export class Template extends BaseModel {
  template_id?: number | null;
  entity_code?: string;
  code?: string;
  title?: string;
  icon?: string;
  clear?: boolean;
  summary?: string;
  bg_color?: string;
  text_color?: string;
  severity?: Severity;
  user_id?: number;
  position?: number;
  is_default?: boolean;
  style?: any;
  data?: any;

  set_data(data: string | any): this {
    this.data = typeof data === 'string' ? JSON.parse(data) : data;
    return this;
  }

  static from(json: AssignObject<Template>): Template {
    return BaseModel.fromJson(Template, json);
  }

  static fromList(data: AssignObject<Template>[]): Template[] {
    return data.flatMap((item) => Template.from(item));
  }
}


export class Templates extends TsMap<string, Template[]> {

  static fromAny(ls: any): Templates {
    if (ls instanceof Templates) return ls;
    else if (ls instanceof Map) return new Templates().putAll(ls);
    else if (typeof ls === 'object') return new Templates().putJson(ls);
    else if (Array.isArray(ls)) return new Templates().putJson(ls);
    else throw new Error(`The value ls_template [${ls}] not support Templates`);
  }


  override get(formCode: string): Template[] {
    return super.get(formCode) ?? [];
  }

  get_template(entity: string, templateId: number): Template {
    return this.computeIfAbsent(entity, k => []).find(s => s.template_id === templateId);
  }

  get_templates(formCode: TemplateCode): Template[] {
    return this.get(formCode) ?? [];
  }

  set_template(data: Template): void {
    const formCode: any = Asserts.notNull(data.entity_code);
    this.computeIfAbsent(formCode, _ => []).push(data);
  }

  get_ticket_def(): Template {
    const templates = this.get_templates('form_ticket') ?? [];
    return templates.find(t => t.is_default) ?? templates[0];
  }

  get_email(): Template[] {
    return this.get_templates('email_ticket');
  }


}