import { FormGroup, FormsBuilder } from "ts-ui/forms";
import { Objects } from "ts-ui/helper";
import { Ticket } from "../../models/ticket";
import { User } from "../../models/user";
import { TicketFormComponent } from "./ticket-form.component";
import { TicketOption } from "../../models/ticket-option";
import { Validators } from "@angular/forms";
import { FormField } from "../../models/form-field";
import { EmailTemplate } from "../../models/template";
const { isTrue, isBlank, notNull, notBlank, isNull, notEmpty } = Objects;

export class Utils {

    public readonly form: FormGroup;
    public emailFields: FormField[] = [];
    public options = TicketOption.createDef();
    public emailTemplate: EmailTemplate;

    constructor(private comp: TicketFormComponent) {
        this.form = this.createFg();
        this.registerListener();
        this.form.reset({options: this.options});
    }

    //-- get control...
    get fb(): FormsBuilder {
        return this.comp.fb;
    }

    get cOption(): FormGroup {
        return this.form.get('options') as any;
    }

    get cEmailObject(): FormGroup {
        return this.form.get('email_object') as FormGroup;
    }


    //--data
    get user(): User { return this.comp.userLogin; }

    get formData(): Partial<Ticket> {
        return this.form.getRawValue();
    }

    get auto_create(): boolean {
        return isTrue(this.options.autoCreate);
    }

    get auto_fill(): boolean {
        return isTrue(this.options.autoFill);
    }

    get is_view_all(): boolean {
        return isTrue(this.options.viewAll);
    }

    get is_email_ticket(): boolean {
        return isTrue(this.options.emailTicket);
    }

    get isPreviewEmailHtml(): boolean {
        return this.is_email_ticket && notNull(this.formData?.email_template);
    }

    get viewTs24(): boolean {
        return isTrue(this.options.viewTs24);
    }

    get isSubjectReadOnly(): boolean {
        const { emailTicket, autoFill } = this.options;
        return (emailTicket === false || autoFill === true);
    }

    copyValue() {
        this.form.reset({
            ...this.formData, ticket_id: undefined
        });
    }

    changeEmailTemplate(template: EmailTemplate): void {
        if(isNull(this.emailTemplate) || !this.emailTemplate.equals(template)) {
            console.log(`changeEmailTemplate: `, this.emailTemplate, template);

            this.emailTemplate = template;

            if (isNull(template)) {
                this.setupFieldEmailTemplate([]);
                this.emailFields = [];
            }
            else if (notBlank(template.data?.html)) {
                const { fields } = template.data;
                this.emailFields = fields;
                this.setupFieldEmailTemplate(fields);

               
            }
        }
    }

    

    

    reset(data: Partial<Ticket>, options?: { onlySelf?: boolean;emitEvent?: boolean; func?: string}): void {
        const opt = {onlySelf: false, emitEvent: true, ...options};
        this.options = this.options.update(data?.options);
        this.form.reset(data, opt);

       
    }

    private createFg(): FormGroup {

        return this.fb.group({
            ticket_id: [null],
            ticket_on: [null],
            full_name: [null],
            tax_code: this.comp.viewTemplate ? [null] : [null, Validators.required],
            company_name: [null],
            phone: [null],
            teamviewer: [null],
            customer_name: [null],
            content_required: [null],
            content_help: [null],
            reception_time: [null],
            complete_time: [null],
            content_copy: [null],
            email: [null],
            subject: [{value: null}],
            body: [null],
            note: [null],
            reply: [null],
            content_email: [null],
            email_template: [null],
            group_help: [null, Validators.required],
            question: [null],
            software: [null, Validators.required],
            chanels: [null, Validators.required],
            support_help: [null, Validators.required],
            soft_name: [null, Validators.required],
            images: [null],
            od_assign: [null, Validators.required],
            od_category_sub: [null, Validators.required],
            od_category: [null, Validators.required],
            od_partner: [null],
            od_partner_id: [{ value: null }],
            od_priority: [null],
            od_replied: [null, Validators.required],
            save_question: [false],
            od_subject_type: [null, Validators.required],
            od_tags: [null],
            od_team: [null, Validators.required],
            od_team_head: [{ value: null, disabled: true }],
            od_ticket_type: [null],
            od_topic: [null],
            options: this.fb.group({
                autoCreate: [null],
                autoFill: [null],
                viewAll: [null],
                viewTs24: [null],
                saveCache: [null],
                emailTicket: [null]
            }),
            email_object: this.fb.group({}),
            edit_note: [true],
            edit_ticket: [true],
            app_id: [null],
        });

    }

    private registerListener(): void {

        this.form.controlValueChange('options', opt => this.options.update(opt));

        //--subject event
        this.form.controlsValueChange(['tax_code', 'support_help', 'ticket_on'], (f, j) => {
            if (this.auto_create && !this.is_email_ticket) {
                f.patchControl('subject', this.createSubject(j));
            }
        });

        //--note
        this.form.controlsValueChange(['support_help', 'complete_time', 'group_help', 'content_help'], (f, j) => {
            if (this.auto_create) f.patchControl('note', this.createNote(j));
        });

        this.form.controlsValueChange(['phone', 'content_required', 'soft_name', 'support_help', 'reception_time'], (f, j) => {
            if (this.auto_create) f.patchControl('body', this.createBody(j));
        });

        this.form.controlsValueChange(['subject', 'body', 'note', 'email', 'tax_code', 'customer_name'], (f, j) => {
            //console.log(`content_copy`, this.auto_create);
            if (this.auto_create) f.patchControl('content_copy', this.createContentCopy());
        });

        this.form.controlsValueChange(['od_partner_id'], (forms, json) => {
            if (isBlank(json['od_partner_id'])) forms.patchControl('od_partner', undefined);
        });

         //--subject event
        this.form.controlValueChange('email_template', v => this.changeEmailTemplate(v));
    }

    private createSubject(formData: Partial<Ticket>): string {
        const { tax_code, support_help, ticket_on } = formData;
        const { room_code, user_code } = this.user;
        if (isBlank(tax_code) || isNull(support_help)) return undefined;
        else return `[${room_code}-${support_help?.code}]-${user_code}-${tax_code}-${ticket_on}`;
    }

    private createNote(formData: Partial<Ticket>): string {
        const { complete_time, group_help, support_help, content_help } = formData;
        const { ts_name } = this.user;

        const lines: string[] = [];


        lines.push(`- Họ tên: ${ts_name}`);

        if (notNull(support_help)) {
            lines.push(`- Hình thức hỗ trợ: ${support_help["value"]}`);
        }

        if (notNull(group_help)) {
            lines.push(`- Nhóm hỗ trợ: ${group_help.title}`);
        }

        if (notBlank(content_help)) {
            lines.push(`- Nội dung đã hỗ trợ: ${content_help} `);
        }

        if (notBlank(complete_time)) {
            lines.push(`- Thời gian hoàn tất: ${complete_time}`);
        }

        if (lines.length > 1) {
            this.form.patchValue({ edit_ticket: true, edit_note: true });
        }

        return lines.join('\n');
    }

    private createBody(formData: Partial<Ticket> = this.formData): string {
        const lines: string[] = [];
        const { phone, content_required, soft_name, support_help, reception_time } = formData;
        const { ts_name } = this.user;

        if (notBlank(phone)) {
            lines.push(`- Số điện thoại liên hệ: ${phone}`);
        }

        if (notBlank(content_required)) {
            let soft_name2 = notBlank(soft_name) ? `${soft_name} - ` : "";
            let support = notNull(support_help) ? ` - ${support_help.support}` : "";
            lines.push(`- Nội dung cần hỗ trợ: ${soft_name2}${content_required}${support}`);
        }

        lines.push(`- Nhân viên gửi thông tin: ${ts_name}`);


        if (notBlank(reception_time)) {
            lines.push(`- Thời gian tiếp nhận: ${reception_time}`);
        }

        if (lines.length > 1) {
            this.form.patchValue({ edit_ticket: true });
        }

        return lines.join('\n');
    }

    /**
     * Create Content copy
     * @param obj {[key: string]: any}
     * @param createIfExist boolean
     * */
    private createContentCopy(options?: { createIfExist?: boolean, subject?: string, body?: string, note?: string }): string {
        //createIfExist = createIfExist || false;
        const formData = this.formData;

        options = Object.assign({}, options);

        const notes: string[] = [];
        const { email, tax_code, customer_name, body, note } = formData;

        //console.log(`body: `, body);

        // email
        if (notBlank(email)) notes.push(`${email}`);

        //-- subject email
        const subject = options.subject ?? this.createSubject(formData);
        if (notBlank(subject)) notes.push(subject);

        if (notBlank(tax_code)) {
            notes.push(`- Mã số thuế / Mã đơn vị: ${tax_code}`);
        }

        if (notBlank(customer_name)) {
            notes.push(`- Tên khách hàng: ${customer_name}`);
        }

        if (notBlank(email)) {
            notes.push(`- Email: ${email}`);
        }

        const bodyFnc = () => this.createBody(formData);
        const noteFnc = () => this.createNote(formData);

        notes.push(options.createIfExist ? bodyFnc() : options.body || body || bodyFnc());
        notes.push(`--------------------------------------------`);
        notes.push(options.createIfExist ? noteFnc() : options.note || note || noteFnc());

        return notes.join(`\n`);
    }

    private setupFieldEmailTemplate(fields: FormField[]) {
        //console.log(`setupFieldEmailTemplate`)

        const emailGroup: FormGroup = this.cEmailObject;

        // remove current field
        Object.keys(emailGroup.controls).forEach(c => emailGroup.removeControl(c));

        // add field to group
        fields.forEach(field => {
            const { name, value, required } = field;
            const control = this.fb.control(value);
            if (!this.comp.viewTemplate && isTrue(required) ) {
                control.addValidators(Validators.required);
            }

            emailGroup.addControl(name, control);
        });

    }

    dateTo(date: Date | number | string, format: string): string {
        return this.comp.datePipe.transform(date, format);
    }
}