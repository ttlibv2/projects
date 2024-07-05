import { Component, ElementRef, ViewChild } from "@angular/core";
import { LayoutService } from "../services/layout.service";
import { MenuItem } from "primeng/api";

@Component({
  selector: "ts-app-topbar",
  templateUrl: "./app-topbar.component.html",
})
export class AppTopbarComponent {
  items!: MenuItem[];

  @ViewChild('menubutton') menuButton!: any;
  @ViewChild('topbarmenubutton') topbarMenuButton!: any;
  @ViewChild('topbarmenu') menu!: ElementRef;

  
  get mobileTopbarActive() {
    return this.layoutService.state.topbarMenuActive;
  }

  get appLogo(): string {
    return this.layoutService.config().appLogo;
  }

  get appName(): string {
    return this.layoutService.config().appName;
  }

  get visibleToggleMenuButton(): boolean {
    return this.layoutService.isVisibleToggleMenuButton();
  }

  constructor(
    public layoutService: LayoutService,
     public el: ElementRef) {}


  onMenuButtonClick() {
    this.layoutService.onMenuToggle();
  }

  onRightMenuButtonClick() {
    this.layoutService.openRightSidebar();
  }

  onMobileTopbarMenuButtonClick() {
    this.layoutService.onTopbarMenuToggle();
  }

  focusSearchInput() {
   // setTimeout(() => {
     // this.searchInput.nativeElement.focus();
   // }, 0);
  }

}
