import { CommonModule } from "@angular/common";
import { AfterContentInit, booleanAttribute, Component, ContentChildren, Input, QueryList, TemplateRef, ViewEncapsulation } from "@angular/core";
import { PrimeTemplate } from "primeng/api";
import {  QueryUtil } from "ts-ui/common";
import { FormLabel } from "./form-label";
import { Objects } from "ts-ui/helper";
import {PropCls} from "ts-ui/common";
const { isTemplateRef, isFalse, notBlank } = Objects;

@Component({
    standalone: true,
    selector: 'ts-form-field',
    imports: [CommonModule, FormLabel],
    encapsulation: ViewEncapsulation.None,
    templateUrl: './form-field.html'
})
export class FormField implements AfterContentInit {
    @Input() inputId: string;
    @Input() fieldClass: PropCls;
    @Input() title: string | TemplateRef<any>;

    /**
     * define is required
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) required: boolean = false;

    /**
     * define required class
     * @group Props
     * */
    @Input() requiredStyleClass: PropCls;

    /**
     * define required text
     * @group Props
     * */
    @Input() requiredText: string = '(*)';

    get field_required(): string | boolean {
        return isFalse(this.required) ? false : notBlank(this.requiredText) ? this.requiredText : true;
    }

   

















    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;

    get titleIsString(): boolean {
        return !isTemplateRef(this.title);
    }

    get fieldCls(): any {
        return {
            ...this.fieldClass
        };
    }

    //--------
    titleTemplate: TemplateRef<any>;

    ngAfterContentInit(): void {

        const resetTemplate = () => {
            this.titleTemplate = undefined;
        };

        QueryUtil.queryList(this.templates, resetTemplate, item => {
            switch (item.getType()) {
                case 'title': this.titleTemplate = item.template; break;
            }
        });


    }



}