import { CommonModule } from "@angular/common";
import { AfterContentInit, Component, ContentChildren, Input, OnDestroy, QueryList, TemplateRef } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { TranslatePipe } from "@ngx-translate/core";
import { PrimeTemplate } from "primeng/api";
import { map, Subject, takeUntil } from "rxjs";
import { AnyTemplateOutlet, QueryUtil, StringTemplate } from "ts-ui/common";
import { JsonAny, Objects } from "ts-ui/helper";

const { isNull, isTemplateRef, isString } = Objects;


@Component({
    standalone: true,
    selector: 'ts-label',
    imports: [CommonModule, TranslatePipe, AnyTemplateOutlet],
    host: { 'class': 'ts-label' },
    template: `

        @if(prefixTemplate) {
            <ng-container *anyTemplate="prefixTemplate">
                <span [class]="prefixTemplate"></span>
            </ng-container>
        }

        @if(icon) {
            <div class="label-icon" [style.display]="'inline-block'">
                <ng-container *anyTemplate="icon">
                    <span [class]="icon"></span>
                </ng-container>
            </div>
        }

        @if(content) {
            <div class="label-content">
                 <ng-container *anyTemplate="content">
                    @if(isI18n()) { {{labelI18n | translate:i18nP}} }
                    @else { <span [innerHTML]="content"></span>}
                </ng-container>
            </div>
        }
        @else {
            <ng-content></ng-content>
        }

         @if(suffixTemplate) {
            <ng-container *anyTemplate="suffixTemplate">
                <span [class]="suffixTemplate"></span>
            </ng-container>
        }

    `
})
export class FormLabel implements AfterContentInit, OnDestroy {

    /**
     * Defined label prefix. (if is string => icon)
     * @groups Props
     * */
    @Input() prefix: StringTemplate;

    /**
     * Defined label suffix. (if is string => icon)
     * @groups Props
     * */
    @Input() suffix: StringTemplate;

    /**
     * Defined label icon
     * @groups Props
     * */
    @Input() icon: StringTemplate;

    /**
     * Defined label content
     * @groups Props
     * */
    @Input() set content(c: StringTemplate) {
        this._labelContent = isNull(c) || isTemplateRef(c) ? c :
            this.sanitizer.sanitize(1, c);
    }

    get content(): StringTemplate {
        return this._labelContent ?? this._pContent;
    }

    /**
     * Defined label i18n params
     * @groups Props
     * */
    @Input() i18nP: JsonAny;

    get prefixTemplate(): any {
        return this.prefix || this._pPrefix;
    }

    get suffixTemplate(): any {
        return this.suffix || this._pSuffix;
    }





    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;

    private _labelContent: any;
    private _pSuffix: TemplateRef<any>;
    private _pPrefix: TemplateRef<any>;
    private _pIcon: TemplateRef<any>;
    private _pContent: TemplateRef<any>;

    private destroy$ = new Subject<boolean>();

    constructor(private sanitizer: DomSanitizer) { }

    get labelI18n(): string {
        return this._labelContent.replace('@@', '');
    }

    isI18n(): boolean {
        return isString(this.content) && this.content.startsWith('@@');
    }

    ngAfterContentInit(): void {
        QueryUtil.queryList(this.destroy$, this.templates,
            () => this.clearTemplate(),
            (item) => this.extractTemplate(item)
        );
    }

    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
        this.clearTemplate();
    }


    private extractTemplate(item: PrimeTemplate) {
        switch (item.getType()) {
            case 'prefix': this._pPrefix = item.template; break;
            case 'suffix': this._pSuffix = item.template; break;
            case 'icon': this._pIcon = item.template; break;
            case 'content': this._pContent = item.template; break;
        }
    }

    private clearTemplate(): void {
        this._pSuffix = undefined;
        this._pPrefix = undefined;
        this._pIcon = undefined;
        this._pContent = undefined;
    }
}

