import { CommonModule } from "@angular/common";
import { AfterContentInit, booleanAttribute, Component, ContentChildren, Input, OnDestroy, QueryList, TemplateRef, ViewEncapsulation } from "@angular/core";
import { SideItem } from "./comps/side-bar";
import { INgStyle, QueryPTemplate } from "ts-ui/common";
import { PrimeTemplate } from "primeng/api";
import { ImageModule } from "primeng/image";
 
@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, ImageModule],
    selector: 'ts-side-panel',
    templateUrl: './view/side-panel.html'
})
export class SidePanel implements AfterContentInit, OnDestroy {

    /**
     * Inline style of the component.
     * @group Props
     */
    @Input() style: INgStyle;

    /**
     * Style class of the component.
     * @group Props
     */
    @Input() styleClass: string;    

    /**
     * Whether multiple tabs can be activated at the same time or not.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) multiple: boolean = false;    

    /**
     * An array of sideitems.
     * @group Props
     */    
    @Input() model: SideItem[];

    /**
     * Inline style of the menu.
     * @group Props
     */
    @Input() menuStyle: INgStyle;

    /**
     * Style class of the menu.
     * @group Props
     */
    @Input() menuClass: string;  

    /**
     * visible pinned of the sidepane
     * @group Props
     */
    @Input({transform: booleanAttribute}) pinned: boolean;

    /**
     * Header of the sidepane
     * @group Props
     */
    @Input() header: TemplateRef<any>;

    /**
     * Footer of the sidepane
     * @group Props
     */
    @Input() footer: TemplateRef<any>;
    
    /**
     * sideitem ref of the sidepane
     * @group Props
     */
    @Input() item: TemplateRef<any>; 

    get headerTemplate(): TemplateRef<any> {
        return this.header || this.pTemplates?.header;
    }

    get footerTemplate(): TemplateRef<any> {
        return this.footer || this.pTemplates?.footer;
    }


    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;
    private queryTemplateHelp: QueryPTemplate;
    private pTemplates: any;

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