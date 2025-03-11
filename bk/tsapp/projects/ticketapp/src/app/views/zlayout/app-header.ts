import { CommonModule } from "@angular/common";
import { AfterViewInit, booleanAttribute, Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewEncapsulation } from "@angular/core";
import { ImageModule } from "primeng/image";
import { PImageDirective } from "./comps/p-image";
import { AppProfile } from "./app-profile";

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, ImageModule, PImageDirective, AppProfile],
    selector: 'ts-app-header',
    styleUrl: './style/_display.scss',
    templateUrl: './view/header.html' 

})
export class AppHeader implements AfterViewInit {

    /**
     * Define src logo
     * */    
    @Input() logo: string = "../assets/images/logo.png";

    /**
     * Define logo name
     * */    
    @Input() logoName: string = 'ConYeu';

    @Input({ transform: booleanAttribute }) visibleLogo: boolean;

    /**
     * Define visible sidebar toggle 
     * */
    @Input({ transform: booleanAttribute }) visibleToggle: boolean = true;

    /**
     * Define toggle icon
     * */
    @Input() toggleIcon: string = 'pi pi-align-left';

    /**
     * Define left pane
     * */
    @Input() leftPane: TemplateRef<any>;

    /**
     * Define right pane
     * */
    @Input() rightPane: TemplateRef<any>;










    @Output() onToggle = new EventEmitter<any>();
    
    toggleClick(event: Event): void {
        this.onToggle.emit(event);
    }

   ngAfterViewInit(): void {
   }

}