
import {Directive,EmbeddedViewRef,Injector,Input,OnChanges,OnDestroy,SimpleChange,SimpleChanges,TemplateRef,ViewContainerRef} from '@angular/core';
import { Objects } from 'ts-ui/helper';

@Directive({
    selector: '[anyTemplate]',
    exportAs: 'anyTemplate',
    standalone: true
})
export class AnyTemplateOutlet<_T = unknown> implements OnChanges, OnDestroy {
    private embeddedViewRef: EmbeddedViewRef<any>;
    private context = new AnyTemplateOutletContext();

    /**
     * A context object to attach to the {@link EmbeddedViewRef}. This should be an
     * object, the object's keys will be available for binding by the local template `let`
     * declarations.
     * Using the key `$implicit` in the context object will set its value as default.
     */
    @Input() anyTemplateContext: any;

    /**
     * A string defining the template reference and optionally the context object for the template.
     */
    @Input() anyTemplate: any | TemplateRef<any>;

    /** Injector to be used within the embedded view. */
    @Input() anyTemplateInjector: Injector;

    constructor(
        private viewContainer: ViewContainerRef,
        private templateRef: TemplateRef<any>) { }

    ngOnChanges(changes: SimpleChanges): void {
        const { anyTemplate } = changes;

        if (anyTemplate) {
            this.context.$implicit = anyTemplate.currentValue;
        }

        /** recreate view when context shape or outlet change **/
        if (this._shouldRecreateView(changes)) this.recreateView();

        /** update context **/
        else this.updateContext();

    }

    ngOnDestroy(): void {
        this.embeddedViewRef?.destroy();
    }

    /**
     * We need to re-create existing embedded view if either is true:
     * - the outlet changed.
     * - the injector changed.
     */
    private _shouldRecreateView(changes: SimpleChanges): boolean {
        const { anyTemplate, anyTemplateContext, anyTemplateInjector } = changes;
        if(Objects.notNull(anyTemplateInjector)) return true;

        let shouldOutletRecreate = false;
        if (anyTemplate) {
            const { previousValue, currentValue, firstChange } = anyTemplate;
            if (firstChange) shouldOutletRecreate = true;
            else {
                const isPreviousOutletTemplate = previousValue instanceof TemplateRef;
                const isCurrentOutletTemplate = currentValue instanceof TemplateRef;
                shouldOutletRecreate = isPreviousOutletTemplate || isCurrentOutletTemplate;
            }
        }

        const shouldContextRecreate = anyTemplateContext && this._hasContextShapeChanged(anyTemplateContext);
        return shouldContextRecreate || shouldOutletRecreate;
    }

    private _hasContextShapeChanged(ctxChange: SimpleChange): boolean {
        const { previousValue, currentValue } = ctxChange ?? {};
        const prevCtxKeys = Object.keys(previousValue || {});
        const currCtxKeys = Object.keys(currentValue || {});

        if (prevCtxKeys.length === currCtxKeys.length) {
            for (const propName of currCtxKeys) {
                if (prevCtxKeys.indexOf(propName) === -1) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    private recreateView(): void {
        this.viewContainer.clear();
        const isTemplateRef = this.anyTemplate instanceof TemplateRef;
        const templateRef = isTemplateRef ? this.anyTemplate : this.templateRef;
        const viewContext = isTemplateRef ? this.anyTemplateContext : this.context;
        this.embeddedViewRef = this.viewContainer.createEmbeddedView(
            templateRef, viewContext, {injector: this.anyTemplateInjector ?? undefined}
        );
    }

    private updateContext(): void {
        const isTemplateRef = this.anyTemplate instanceof TemplateRef;
        const newCtx = isTemplateRef ? this.anyTemplateContext : this.context;
        const oldCtx = this.embeddedViewRef!.context as any;
        if (newCtx) {
            for (const propName of Object.keys(newCtx)) {
                oldCtx[propName] = newCtx[propName];
            }
        }
    }

    static ngTemplateContextGuard<T>(_dir: AnyTemplateOutlet<T>, _ctx: any): _ctx is AnyTemplateOutletContext {
        return true;
    }
}

export class AnyTemplateOutletContext {
    public $implicit: any;
}