import { CommonModule } from "@angular/common";
import { AfterContentInit, Component, ContentChildren, Input, QueryList, TemplateRef, ViewEncapsulation } from "@angular/core";
import { PrimeTemplate } from "primeng/api";

@Component({
    standalone: true,
    selector: 'ts-toolbar',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule],
    templateUrl: './toolbar.html',
    host: { 'class': 'p-element'}
})
export class ToolBar implements AfterContentInit {
    @Input() flexDirection: 'column' | 'row' = 'row';
    @Input() flexGap: string = '.5rem';

    /**
    * Inline style of the component.
    * @group Props
    */
    @Input() style: { [klass: string]: any } | null | undefined;

    /**
     * Style class of the component.
     * @group Props
     */
    @Input() styleClass: string | undefined;


    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;

    leftTemplate: TemplateRef<any>;
    rightTemplate: TemplateRef<any>;
    centerTemplate: TemplateRef<any>;
    
    ngAfterContentInit(): void {
        this.templates?.changes.subscribe(lt => this.readTemplates(lt));
        this.readTemplates(this.templates);
    }

    private readTemplates(templates: QueryList<PrimeTemplate>) {
        this.leftTemplate = undefined;
        this.rightTemplate = undefined;
        this.centerTemplate = undefined;

        templates?.forEach(item => {
            switch(item.getType()) {
                case 'left': this.leftTemplate = item.template; break;
                case 'right': this.rightTemplate = item.template; break;
                case 'center': this.centerTemplate = item.template; break;
            }
        })
    }

    ngStyle(): any {
        return {
            [`flex-direction`]: this.flexDirection
        }
    }
}