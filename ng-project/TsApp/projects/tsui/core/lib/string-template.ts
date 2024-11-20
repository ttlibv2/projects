
import { Directive, EmbeddedViewRef, Injector, Input, OnChanges, Renderer2, SimpleChange, SimpleChanges, TemplateRef, ViewContainerRef } from '@angular/core';
import { Objects } from 'ts-ui/helper';

const { notNull } = Objects;


@Directive({
    selector: '[tsTemplateOutlet]',
    exportAs: 'tsTemplateOutlet',
    standalone: true
})
export class StringTemplateOutlet<C = unknown> implements OnChanges {
    private embeddedViewRef: EmbeddedViewRef<any> | null = null;
    private context = new tsTemplateOutletContext();

    /**
     * A context object to attach to the {@link EmbeddedViewRef}. This should be an
     * object, the object's keys will be available for binding by the local template `let`
     * declarations.
     * Using the key `$implicit` in the context object will set its value as default.
     */
    @Input() tsTemplateOutletContext: C | null = null;

    /**
     * A string defining the template reference and optionally the context object for the template.
     */
    @Input() tsTemplateOutlet: any | TemplateRef<C> | null = null;

    /** Injector to be used within the embedded view. */
    @Input() tsTemplateOutletInjector: Injector | null = null;

    constructor(
        private viewContainer: ViewContainerRef,
        private templateRef: TemplateRef<any>,
        private renderer: Renderer2) { }


    ngOnChanges(changes: SimpleChanges): void {
        const { tsTemplateOutlet } = changes;

        if (tsTemplateOutlet) {
            this.context.$implicit = tsTemplateOutlet.currentValue;
        }

        // recreate view when context shape or outlet change
        if (this._shouldRecreateView(changes)) this.recreateView();

        //update context
        else this.updateContext();

    }

    private _shouldRecreateView(changes: SimpleChanges): boolean {
        const { tsTemplateOutletContext, tsTemplateOutlet } = changes;
        const { previousValue, currentValue, firstChange } = tsTemplateOutlet || {};

        let shouldOutletRecreate = false;
        if (notNull(tsTemplateOutlet)) {
            if (firstChange) shouldOutletRecreate = true;
            else {
                const isPreviousOutletTemplate = previousValue instanceof TemplateRef;
                const isCurrentOutletTemplate = currentValue instanceof TemplateRef;
                shouldOutletRecreate = isPreviousOutletTemplate || isCurrentOutletTemplate;
            }
        }

        const hasContextShapeChanged = (ctxChange: SimpleChange): boolean => {
            const { previousValue, currentValue } = ctxChange;

            const prevCtxKeys = Object.keys(previousValue || {});
            const currCtxKeys = Object.keys(currentValue || {});

            if (prevCtxKeys.length === currCtxKeys.length) {
                for (const propName of currCtxKeys) {
                    if (prevCtxKeys.indexOf(propName) === -1) {
                        return true;
                    }
                }
                return false;
            }
            else return true;
        };

        const shouldContextRecreate = tsTemplateOutletContext && hasContextShapeChanged(tsTemplateOutletContext);
        return shouldContextRecreate || shouldOutletRecreate;
    }


    private recreateView(): void {
        this.viewContainer.clear();
        const isTemplateRef = this.tsTemplateOutlet instanceof TemplateRef;
        const templateRef: any = isTemplateRef ? this.tsTemplateOutlet : this.templateRef;
        const context: any = isTemplateRef ? this.tsTemplateOutletContext : this.context;
        this.embeddedViewRef = this.viewContainer.createEmbeddedView(templateRef, context);
    }

    private updateContext(): void {
        const isTemplateRef = this.tsTemplateOutlet instanceof TemplateRef;
        const newCtx: any = isTemplateRef ? this.tsTemplateOutletContext : this.context;
        const oldCtx: any = this.embeddedViewRef!.context;
        if (newCtx) {
            for (const propName of Object.keys(newCtx)) {
                oldCtx[propName] = newCtx[propName];
            }
        }
    }
}

export class tsTemplateOutletContext {
    public $implicit: any;
}