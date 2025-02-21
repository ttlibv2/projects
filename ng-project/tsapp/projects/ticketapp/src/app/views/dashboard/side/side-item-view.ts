import { AfterContentInit, AfterViewInit, ChangeDetectionStrategy, Component, ContentChildren, Input, numberAttribute, OnChanges, OnDestroy, QueryList, SimpleChanges, TemplateRef, ViewEncapsulation } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { BadgeModule } from "primeng/badge";
import { TooltipModule } from "primeng/tooltip";
import { RippleModule } from "primeng/ripple";
import { SideItem } from "./side.interface";
import { PrimeTemplate } from "primeng/api";
import { QueryPTemplate } from "ts-ui/common";
import { Objects } from "ts-ui/helper";
const {isNull, isEmpty} = Objects;

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CommonModule, RouterModule, BadgeModule, TooltipModule, RippleModule],
    selector: '[ts-side-item]',
    templateUrl: './view/side-item2.html',
    host: {
        'class': 'side-item',
    },
})
export class SideItemView implements OnChanges, AfterContentInit, OnDestroy {
    @Input() model: SideItem;
    @Input({transform: numberAttribute}) level: number;
    @Input() badgeTemplate: TemplateRef<any>;
    @Input() contentTemplate: TemplateRef<any>;

    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;
    private queryTemplateHelp: QueryPTemplate;
    private pTemplates: any;

    get hasMain(): boolean {
        const {model, level} = this;
        const level0 = isNull(level) || level <= 0;
        const noUrl = isNull(model.routerLink) && isNull(model.url) ;
        return level0 && noUrl && isEmpty(model.items);
    }


    ngOnChanges(changes: SimpleChanges): void {

    }

    ngAfterContentInit(): void {
        this.queryTemplateHelp = QueryPTemplate.register({
            templates: this.templates,
            onClear: () => this.pTemplates = undefined,
            onUpdate: tv => this.pTemplates = tv
        })
    }

    ngOnDestroy(): void {
        this.queryTemplateHelp?.destroy();
    }
}