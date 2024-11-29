
import {booleanAttribute, Component, ComponentRef, Directive,EmbeddedViewRef,inject,Injector,Input,OnChanges,OnDestroy,SimpleChange,SimpleChanges,TemplateRef,ViewContainerRef} from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { Objects } from 'ts-ui/helper';
const {isNull, parseI18N, isFalse} = Objects;

@Component({
    selector: 'dynamic-i18n',
    template: `{{text}}`
})
class DynamicI18N {
    @Input() text: string;
}

@Directive({
    selector: '[anyTemplate]',
    exportAs: 'anyTemplate',
    providers: [TranslatePipe],
    standalone: true
})
export class AnyTemplateOutlet<_T = unknown> implements OnChanges, OnDestroy {
    private embeddedViewRef: EmbeddedViewRef<any>;
    private componentViewRef: ComponentRef<any>;
    private context = new AnyTemplateOutletContext();
    private translatePipe = inject(TranslatePipe, { self: true, optional: true });

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
    @Input() anyTemplate: any | string | TemplateRef<any>;

    /** Injector to be used within the embedded view. */
    @Input() anyTemplateInjector: Injector;

    /**
     * defined i18n has support 
     * */
    @Input({ transform: booleanAttribute }) 
    anyTemplateUseI18n: boolean = false; 



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
        this.componentViewRef?.destroy();
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
        this.embeddedViewRef?.destroy();
        this.componentViewRef?.destroy();

        const hasTemplate = this.anyTemplate instanceof TemplateRef;
        const i18nKey: string = isFalse(hasTemplate) && this.anyTemplateUseI18n ? parseI18N(this.anyTemplate) : undefined;

        if (hasTemplate || isNull(i18nKey)) {
            const templateRef = hasTemplate ? this.anyTemplate : this.templateRef;
            const viewContext = hasTemplate ? this.anyTemplateContext : this.context;
            this.embeddedViewRef = this.viewContainer.createEmbeddedView(
                templateRef, viewContext, { injector: this.anyTemplateInjector }
            );
        }
        else {
            let i18nValue = this.translatePipe.transform(i18nKey);
            this.componentViewRef = this.viewContainer.createComponent(DynamicI18N, { injector: this.anyTemplateInjector });
            this.componentViewRef.setInput('text', i18nValue);
        }
       
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