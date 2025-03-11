import { CommonModule } from "@angular/common";
import { AfterContentInit, booleanAttribute, Component, ContentChildren, Input, OnDestroy, QueryList, TemplateRef, ViewEncapsulation } from "@angular/core";
import { PrimeTemplate } from "primeng/api";
import { INgStyle, QueryUtil, StringTemplate } from "ts-ui/common";
import { FormLabel } from "./form-label";
import { Objects } from "ts-ui/helper";
import {INgClass} from "ts-ui/common";
import { TranslateModule } from "@ngx-translate/core";
import { Subject } from "rxjs";
import {ReactiveFormsModule} from "@angular/forms";
const { isTemplateRef, isString, isNull, parseI18N } = Objects;

@Component({
    standalone: true,
    selector: 'ts-form-field',
    imports: [CommonModule, FormLabel, TranslateModule,
        PrimeTemplate, ReactiveFormsModule,],
    encapsulation: ViewEncapsulation.None,
    templateUrl: './form-field.html'
})
export class FormField implements AfterContentInit, OnDestroy {
    
    /**
     * Define id field
     * @group Props
     * */    
    @Input() set inputId(id: string) {
        this._inputId = id;
    }

    get inputId(): string {
        return this._inputId || this.labelAsId();
    }

    @Input() fieldClass: INgClass;

    @Input() label: StringTemplate;

    /**
     * Define is required
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) required: boolean = false;

    /**
     * define required class
     * @group Props
     * */
    @Input() requiredClass: INgClass;

    /**
     * define required style
     * @group Props
     * */
    @Input() requiredStyle: INgStyle;    

    /**
     * define required text
     * @group Props
     * */
    @Input() requiredText: string = '(*)';

    @Input() fieldTag: string;

    //


   

















    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;

    private destroy$ = new Subject<boolean>();
    private _inputId: string;

    get labelIsString(): boolean {
        return !isTemplateRef(this.label);
    }


    //--------
    labelTemplate: TemplateRef<any>;


    ngAfterContentInit(): void {

        const resetTemplate = () => {
            this.labelTemplate = undefined;
        };

        QueryUtil.queryList(this.destroy$, this.templates, resetTemplate, item => {
            switch (item.getType()) {
                case 'label': this.labelTemplate = item.template; break;
            }
        });


    }

    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
    }


    private labelAsId(): string {
        const text = parseI18N(this.label);
        if(isNull(text)) return undefined;
        else {
            let index = text.lastIndexOf('.');
            return index < 0 ? text : text.substring(index +1);
        }
    }

}