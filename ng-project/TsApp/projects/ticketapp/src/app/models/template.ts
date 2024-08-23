import {TsMap, Objects, JsonAny} from "ts-ui/helper";
import {BaseModel} from "./base-model";
import {AssignObject} from "./common";
import { Severity } from "ts-ui/common";

export type TemplateThreadCode = 'form_ticket' | 'email_ticket';


export class Template extends BaseModel {
    template_id?: number | null;
    thread?: string;
    code?: string;
    title?: string;
    summary?: string;
    user_id?: number;
    position?: number;
    is_default?: boolean;
    custom?: JsonAny;
    data?: JsonAny;

    set_data(data: JsonAny): this {
        this.data = data;
        return this;
    }

    set_thread(code: string): this {
        this.thread = code;
        return this;
    }

    static from(object: AssignObject<Template>): Template {
        if(object instanceof Template) return object;
        else {
            const {thread} = object;
            if('form_ticket' == thread) return TicketTemplate.from(object);
            else if('email_ticket' == thread) return EmailTemplate.from(object);
            else return BaseModel.fromJson(Template, object);
        }
    }

    static fromList(data: AssignObject<Template>[]): Template[] {
        return data.flatMap((item) => Template.from(item));
    }
    
}

export class TicketTemplate extends Template {

    override data?: {
        software_id?: number;
        chanel_ids?: number[];
        group_help_id?: number;
        support_help_id?: number;
        soft_name?: string;
        team_id?: number;
        assign_id?: number;
        subject_type_id?: number;
        category_id?: number;
        category_sub_id?: number;
        team_head_id?: number;
        priority_id?: number;
        tag_ids?: number[];
        replied_id?: number;
        ticket_type_id?: number;
        options?: JsonAny;
    }

    override custom?: {
        icon?: string;
        clear?: boolean;
        bg_color?: string;
        text_color?: string;
        severity?: Severity;
    }

    static fromObject(json: AssignObject<TicketTemplate>): TicketTemplate {
        return BaseModel.fromJson(TicketTemplate, json);
    }
}

export class EmailTemplate extends Template {

    override data?: {
        html?: string;
        fields?: EmailTemplateField[];
    }

    static fromObject(json: AssignObject<EmailTemplate>): EmailTemplate {
        return BaseModel.fromJson(EmailTemplate, json);
    }

}



export class Templates {

    // TsMap<threadCode, Template[]>
    readonly map = new TsMap<string, Template[]>();

    static fromAny(ls: any): Templates {
        if (ls instanceof Templates) {    return ls;  } 

        // {[thread]: AssignObject<Template>[]}
        else if (Objects.isObject(ls)) {
            return new Templates().putObject(ls);
        } 
        // AssignObject<Template>[]
        else if (Array.isArray(ls)) {
            const templates = new Templates();
            ls.forEach(js => templates.add(js));
            return templates;
        }//
        else throw new Error(`The value ls_template [${ls}] not support Templates`);
    }

    get(thread: TemplateThreadCode): Template[] {
        return this.map.computeIfAbsent(thread, _ => []);
    }

    add(object: AssignObject<Template>): void {
        const template = Template.from(object);
        this.get(<any>template.thread).push(template);
    }

    set(thread: string, objects: AssignObject<Template>[]): void {
        const templates = objects.map(object => Template.from(object).set_thread(thread));
        this.map.set(thread, templates);
    }

    putObject(object: {[thread: string]: AssignObject<Template>[]}): this {
        Object.keys(object).forEach(key => this.add(object[key]));
        return this;
    }
    
    get_template(thread: string, keyword: string | number): Template {
        const templates = this.map.computeIfAbsent(thread, ec => []);
        if(typeof keyword === 'number') return templates.find(t => t.template_id === keyword);
        else return templates.find(t => t.code === keyword);
    }

    get_email(): EmailTemplate[] {
        return this.get('email_ticket');
    }

    get_ticket(): EmailTemplate[] {
        return this.get('form_ticket');
    }


}

export class EmailTemplateField {
    name: string;
    type?: string;
    label: string;
    required?: boolean;
    defaultValue?: any;
}

export interface TicketTemplateData {
    software_id?: number;
    chanel_ids?: number[];
    group_help_id?: number;
    support_help_id?: number;
    soft_name?: string;
    team_id?: number;
    assign_id?: number;
    subject_type_id?: number;
    category_id?: number;
    category_sub_id?: number;
    team_head_id?: number;
    priority_id?: number;
    tag_ids?: number[];
    replied_id?: number;
    ticket_type_id?: number;
    options?: JsonAny;
  
  }