import { CommonModule } from "@angular/common";
import {
    AfterContentInit, ChangeDetectionStrategy, Component, ContentChildren,
    Input, OnChanges, OnDestroy, QueryList, SimpleChanges, TemplateRef, ViewEncapsulation
} from "@angular/core";
import { PrimeTemplate } from "primeng/api";
import { Subject, takeUntil } from "rxjs";
import { PropCls, StyleCls } from "ts-ui/common";


@Component({
    standalone: true,
    selector: 'ts-border-panel',
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CommonModule],
    templateUrl: './border-panel.html'
})
export class BorderPanel implements OnChanges, OnDestroy, AfterContentInit {
    @Input() styleClass: PropCls;
    @Input() style: StyleCls;

    @Input() top: TemplateRef<any>;
    @Input() left: TemplateRef<any>;
    @Input() center: TemplateRef<any>;
    @Input() right: TemplateRef<any>;
    @Input() bottom: TemplateRef<any>;

    @Input() topStyleClass: PropCls;
    @Input() topStyle: StyleCls;

    @Input() leftStyleClass: PropCls;
    @Input() leftStyle: StyleCls;

    @Input() rightStyleClass: PropCls;
    @Input() rightStyle: StyleCls;

    @Input() centerStyleClass: PropCls;
    @Input() centerStyle: StyleCls;

    @Input() bottomStyleClass: PropCls;
    @Input() botttomStyle: StyleCls;

    get topTemplate(): TemplateRef<any> {
        return this._topTemplate ?? this.top;
    }

    get bottomTemplate(): TemplateRef<any> {
        return this._bottomTemplate ?? this.bottom;
    }

    get leftTemplate(): TemplateRef<any> {
        return this._leftTemplate ?? this.left;
    }

    get rightTemplate(): TemplateRef<any> {
        return this._rightTemplate ?? this.right;
    }

    get centerTemplate(): TemplateRef<any> {
        return this._centerTemplate ?? this.center;
    }

    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;

    private destroy$ = new Subject();
    private _topTemplate: TemplateRef<any>;
    private _leftTemplate: TemplateRef<any>;
    private _rightTemplate: TemplateRef<any>;
    private _centerTemplate: TemplateRef<any>;
    private _bottomTemplate: TemplateRef<any>;

    ngOnChanges(changes: SimpleChanges): void {

    }

    ngAfterContentInit(): void {
        this.extractTemplates(this.templates);
        this.templates.changes.pipe(takeUntil(this.destroy$))
            .subscribe(items => this.extractTemplates(items));
    }

    ngOnDestroy(): void {
        this.destroy$.next(null);
        this.destroy$.complete();
        this.clearTemplates();
    }

    private extractTemplates(list: QueryList<PrimeTemplate>) {
        this.clearTemplates();
        list?.forEach(item => {
            switch (item.getType()) {
                case 'top': this._topTemplate = item.template; break;
                case 'left': this._leftTemplate = item.template; break;
                case 'right': this._rightTemplate = item.template; break;
                case 'center': this._centerTemplate = item.template; break;
                case 'bottom': this._bottomTemplate = item.template; break;

            }
        });
    }

    private clearTemplates(): void {
        this._topTemplate = undefined;
        this._leftTemplate = undefined;
        this._rightTemplate = undefined;
        this._centerTemplate = undefined;
        this._bottomTemplate = undefined;
    }


}
