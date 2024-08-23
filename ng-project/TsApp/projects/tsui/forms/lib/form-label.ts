import {
    AfterContentInit,
    booleanAttribute,
    Component,
    ContentChildren,
    Directive,
    Input, OnChanges,
    OnDestroy,
    QueryList, SimpleChanges,
    TemplateRef
} from "@angular/core";
import {Subject} from "rxjs";
import {TooltipPos} from "ts-ui/common";
import {PrimeTemplate, TooltipOptions} from "primeng/api";
import {Nullable} from "primeng/ts-helpers";

@Directive({
    selector: '[ts-form-label]',
   // templateUrl: `./html/form-label.html`
})
export class FormLabel implements OnChanges, OnDestroy, AfterContentInit {

    /**The for property of label*/
    @Input() for?: string;

    /**add required style to current item*/
    @Input({transform:booleanAttribute})
    required: boolean = false;

    /**whether to display : after label text.*/
    @Input({transform:booleanAttribute})
    colon: boolean = true;

    /**	Set tooltip info*/
    @Input() tooltip: string;

    /**	Set icon of tooltip info */
    @Input() tooltipIcon: string

    @Input() tooltipPos: TooltipPos;

    @Input() tooltipOption: TooltipOptions;

    /**The text align of label*/
    @Input() align: 'left' | 'right' = 'right';

    /**whether label can be wrap*/
    @Input({transform:booleanAttribute})
    wrap: boolean = false;

    private destroy$ = new Subject<boolean>();

    @ContentChildren(PrimeTemplate)
    templates: QueryList<PrimeTemplate>;

    tooltipIconTemplate: Nullable<TemplateRef<any>>;


    ngAfterContentInit() {
        this.templates?.forEach(item => {
            switch (item.getType()) {
                case 'tooltipIcon': {
                    this.tooltipIconTemplate = item.template;
                    break;
                }
            }
        });

    }

    ngOnChanges(changes: SimpleChanges) {
    }

    ngOnDestroy() {
        this.destroy$.next(true);
        this.destroy$.complete();
    }
}