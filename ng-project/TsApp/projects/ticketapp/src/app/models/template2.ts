import {BaseModel} from "./base-model";
import {AssignObject, Callback, TsMap, Asserts} from "ts-ui/helper";

export abstract class Template<TData=any> extends BaseModel {
    template_id?: number;
    title?: string;
    summary?: string;
    is_default?: boolean;
    shared?: boolean;
    user_id?: number;
    position?: number;
    data?: TData;

    abstract get thread(): string;
}

export abstract class Templates<TData, TP extends Template<TData>> {
    private readonly map = new TsMap<number, TP>();

    get(templateId: number): TP {
        return this.map.get(templateId);
    }

    set(template: TP): this {
        const tempId = Asserts.notNull(template?.template_id);
        this.map.set(tempId, template);
        return this;
    }

    

    protected abstract get newItemFnc(): Callback<AssignObject<TP>, TP>;






}

export interface TicketDefaultData {
    assign_id?: number;
    soft_name?: string;
    team_id?: number;
    tag_ids?: number[];
    chanel_ids?: number[];
    repiled_id?: number;
    category_id?: number;
    category_sub_id?: number;
    priority_id?: number;
    software_id?: number;
    team_head_id?: number;
    group_help_id?: number;
    ticket_type_id?: number;
    subject_type_id?: number;
    support_help_id?: number;
    options?: {
        viewAll?: boolean;
        autoFill?: boolean;
        viewTS24?: boolean;
        autoCreate?: boolean;
        emailTicket?: boolean;
    }
}

export class TicketDefaultTemplate extends Template<TicketDefaultData> {

    static fromObject(data: AssignObject<TicketDefaultTemplate>): TicketDefaultTemplate {
        return BaseModel.fromJson(TicketDefaultTemplate, data);

    }

    style: {
        bg_color?: string;
        text_color?: string;
        severity?: string;
    };

    override get thread(): string {
        return "form_ticket";
    }

}











export class EmailTemplate extends Template<{}> {

    override get thread(): string {
        return "email_ticket";
    }

}