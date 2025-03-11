import { CommonModule } from "@angular/common";
import { booleanAttribute, Component, Input, ViewEncapsulation } from "@angular/core";
import { AppHeader } from "./app-header";
import { RouterModule } from "@angular/router";
import { InputGroupModule } from "ts-ui/inputgroup";
import { InputTextModule } from "primeng/inputtext"; 
import { SidePanel } from "./side-pane";
import { I18NPipe, INgStyle } from "ts-ui/common";
import { Objects } from "ts-ui/helper";
import { BreadcrumbModule } from "primeng/breadcrumb";
import { MenuItem } from "primeng/api";
import { BreadcrumbService } from "./services/breadcrumb-srv";
import { Observable } from "rxjs";

const { isNull } = Objects;

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, RouterModule, AppHeader,
         InputGroupModule, InputTextModule, SidePanel, BreadcrumbModule, 
         I18NPipe],
    selector: 'ts-app-layout',
    templateUrl: './view/layout.html'

}) 
export class AppLayout {

    @Input() styleClass: string;
    @Input() style: INgStyle;

    /**
     * vertical => column
     * horizontal => row
     * */
    @Input() orientation: 'vertical' | 'horizontal' = 'vertical';

    /**
     * Define visible footer
     * */
    @Input({ transform: booleanAttribute }) visibleFooter: boolean;

    /**
     * Define visible topbar
     * */
    @Input({ transform: booleanAttribute }) visibleTopbar: boolean;

    homeBcMenu: MenuItem = {
        icon: 'pi pi-home',
        iconClass: 'text-info',
        routerLink: '/'
    }

    breadcrumbs$: Observable<MenuItem[]>;

    constructor(private bcService: BreadcrumbService) {
        this.breadcrumbs$ = bcService.breadcrumbs$;
    }

    containerCls(): any {
        const { orientation: or} = this;

        return {
            [`layout-container`]: true,
            [`layout-container-vertical`]: isNull(or) || or === 'vertical',
            [`layout-container-horizontal`]: or === 'horizontal',
        }
    }
}