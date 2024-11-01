import { AfterContentInit, booleanAttribute, ChangeDetectionStrategy, Component, ContentChildren, Input, NgZone, OnChanges, Optional, QueryList, SimpleChanges, TemplateRef, ViewEncapsulation } from "@angular/core";
import { PrimeTemplate } from "primeng/api";
import { Objects } from "ts-ui/helper";
const { isString, notBlank, notNull, anyTrue, isNull } = Objects;
const cls = 'ts-inputgroup';

function hasBool(array: any[], template: TemplateRef<any>): boolean {
    if ((array || []).length > 0) return true;
    else if (isNull(template)) return false;
    //else return notNull(template?.elementRef?.nativeElement?.nextSibling);
    else return true;
}

export interface IconObject {
    name: string;
    onClick?: () => void;
}

@Component({
    preserveWhitespaces: true,
    selector: cls,
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './input-group.html',
    //template: ` `
})
export class InputGroup implements AfterContentInit, OnChanges {
    @Input() prefixIcons: (string | IconObject)[] = [];
    @Input() suffixIcons: string[] = [];
    @Input() addonAfterIcons: string[] = [];
    @Input() addonBeforeIcons: string[] = [];

    @Input({ transform: booleanAttribute })
    compact: boolean = true;

    /**
     * placeholder of the input element.
     * @group Props
     */
    @Input() placeholder: string = '';

    /**
     * Class of the element.
     * @group Props
     */
    @Input() styleClass: string;

    /**
     * Class of the element.
     * @group Props
     */
    @Input() style: { [key: string]: any };

    @Input() flexAddon: string = '.5rem';
    @Input() flexAffix: string = '.5rem';


    get hasPrefix(): boolean {
        return hasBool(this.prefixIcons, this.prefixTemplate);
    }

    get hasSuffix(): boolean {
        return hasBool(this.suffixIcons, this.suffixTemplate);
    }

    get hasAddonBefore(): boolean {
        return hasBool(this.addonBeforeIcons, this.addonBeforeTemplate);
    }

    get hasAddonAfter(): boolean {
        return hasBool(this.addonAfterIcons, this.addonAfterTemplate);
    }

    prefixTemplate: TemplateRef<any>;
    suffixTemplate: TemplateRef<any>;
    addonAfterTemplate: TemplateRef<any>;
    addonBeforeTemplate: TemplateRef<any>;
    fieldTemplate: TemplateRef<any>;


    @ContentChildren(PrimeTemplate)
    private templates: QueryList<PrimeTemplate>;

    constructor(
        @Optional() private ngZone: NgZone) { }

    ngIconClass(icon: string | IconObject) {
        return isString(icon) ? icon : icon.name;
    }

    iconClick(icon: string | IconObject) {
        if (icon && !isString(icon)) {
            this.ngZone.run(() => icon.onClick());
        }
    }


    ngAfterContentInit(): void {
        this.templates?.forEach(item => {
            switch (item.getType()) {
                case 'field':
                    this.fieldTemplate = item.template;
                    break;

                case 'prefix':
                    this.prefixTemplate = item.template;
                    break;

                case 'suffix':
                    this.suffixTemplate = item.template;
                    break;

                case 'addonBefore':
                    this.addonBeforeTemplate = item.template;
                    break;

                case 'addonAfter':
                    this.addonAfterTemplate = item.template;
                    break;
            }
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
    }

    containerCls(): any {
        return {
            [cls]: true,
            [`${cls}-compact`]: this.compact === true,
            [`has-addon-before`]: this.hasAddonBefore,
            [`has-addon-after`]: this.hasAddonAfter,
            [`has-prefix`]: this.hasPrefix,
            [`has-suffix`]: this.hasSuffix,
            [this.styleClass]: notBlank(this.styleClass),

        };
    }

    containerStyleCls(): any {
        return {
            ...this.style,
            [`column-gap`]: anyTrue(this.hasAddonAfter, this.hasAddonBefore) ? this.flexAddon : undefined
        }
    }

    styleAddonCls(): any {
        return {
            [`column-gap`]: this.flexAddon
        }
    }

    styleInputCls(): any {
        return {
            [`column-gap`]: this.flexAffix
        }
    }

}