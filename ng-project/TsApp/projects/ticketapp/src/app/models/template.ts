import { Asserts, AssignObject, Callback, Objects, TsMap } from "ts-ui/helper";
import { BaseModel } from "./base-model";
import { FormField } from "./form-field";

export type CallbackAssign<E> = Callback<AssignObject<E>, E>;
export type TemplateThread = 'ticket_template' | 'email_template';

const {isTrue} = Objects;

export class Template<D = any> extends BaseModel {
    template_id?: number;
    thread: string;
    title?: string;
    summary?: string;
    shared?: boolean;
    position?: number;
    is_default?: boolean;
    user_id?: number;
    data?: D;

    equals(o: any): boolean {
        if(o instanceof Template) return this === o || this.template_id === o.template_id;
        else return false;
    }

    static fromAll(data: AssignObject<Template>): Template {
        const thread = Asserts.notNull(data.thread);
        if(thread === 'ticket_template') return TicketTemplate.from(data);
        else if(thread === 'email_template') return EmailTemplate.from(data);
        else return BaseModel.fromJson(Template, data);
    }

}

export class TemplateMap<T extends Template = any> {
    private readonly map = new TsMap<number, T>();

    protected get newFunc(): CallbackAssign<T> {
        return object => <any>Template.fromAll(object);
    }


    /**
     * Returns template without id
     * @param {number} templateId 
     * @returns T
     * */
    get(templateId: number): T {
        return this.map.get(templateId);
    }

    /**
     * Set template 
     * @param {T} template the template
     * */
     set(template: AssignObject<T>): this {
        const newObj = this.newFunc(template);
        Asserts.notNull(newObj?.template_id, "The template_id is null");
        this.map.set(newObj.template_id, newObj);
        return this;
    }

    /**
     * Set list template 
     * @param templates the list template
     * */
    set_all(templates: AssignObject<T>[]): this {
        //console.log(`templates: `, templates);
        templates.forEach(template => this.set(template));
        return this;
    }
    
    /**
     * Delete template by id
     * @param templateId {number}
     * */
    delete(templateId: number): this {
        this.map.delete(templateId);
        return this;
    }

    /**
     * Returns list template
     * @returns T[] 
     * */
    list(): T[] {
        return this.map.get_values();
    }

    get_default(): T {
        return this.list().find(t => t.is_default);
    }

}

//=======================================================
//   EmailTemplate
//=======================================================


export class EmailTemplateMap extends TemplateMap<EmailTemplate> {

    protected override get newFunc(): CallbackAssign<EmailTemplate> {
        return object => EmailTemplate.from(object);
    }
}

export class EmailTemplate extends Template<EmailTemplateData> {
    override thread: 'email_template';

    override update(object: AssignObject<this>): this {
        return super.update(object);
    }
    
    static from(data: AssignObject<EmailTemplate>):EmailTemplate {
        return BaseModel.fromJson(EmailTemplate, data);
    }

}

export interface EmailTemplateData {
    html: string;
    fields: FormField[];
}


//=======================================================
//   EmailTemplate
//=======================================================

export class TicketTemplateMap extends TemplateMap<TicketTemplate> {

    protected override get newFunc(): CallbackAssign<TicketTemplate> {
        return object => TicketTemplate.from(object);
    }

    get_emailDefault(): TicketTemplate {
        const all = this.list().filter(t => t.emailTicket);
        return all.find(t => isTrue(t.is_default)) || all[0];
    }

}


export class TicketTemplate extends Template<TicketTemplateData> {
    override thread: 'form_template';

    severity?: string;
    text_color?: string;
    bg_color?: string;
    icon?: string;
    clear?: boolean;

    get emailTicket(): boolean {
        return isTrue(this.data?.options?.emailTicket);
    }


    static from(json: AssignObject<TicketTemplate>): TicketTemplate {
        return BaseModel.fromJson(TicketTemplate, json);
    }


}

export interface TicketTemplateData {
    tag_ids?: number[];
    team_id?: number;
    assign_id?: number;
    soft_name?: string;
    chanel_ids?: number[];
    replied_id?: number;
    category_id?: number;
    category_sub_id?: number;
    priority_id?: number;
    software_id?: number;
    team_head_id?: number;
    group_help_id?: number;
    ticket_type_id?: number;
    subject_type_id?: number;
    support_help_id?: number;
    email_template_id?: number;

    options?: {
        viewAll?: boolean;
        autoFill?: boolean;
        viewTs24?: boolean;
        autoCreate?: boolean;
        emailTicket?: boolean;
    }
}