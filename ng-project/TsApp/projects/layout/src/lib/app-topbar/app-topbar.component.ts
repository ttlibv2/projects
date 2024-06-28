import { Component, ElementRef, ViewChild } from "@angular/core";
import { LayoutService } from "../services/layout.service";
import { MenuItem } from "primeng/api";

@Component({
  selector: "ts-app-topbar",
  templateUrl: "./app-topbar.component.html",
})
export class AppTopbarComponent {
  items!: MenuItem[];

  @ViewChild('menubutton') menuButton!: ElementRef;
  @ViewChild('topbar_mobile_menubutton') topbarMenuButton!: ElementRef;
  @ViewChild('topbarmenu') menu!: ElementRef;

  constructor(public layoutService: LayoutService, public el: ElementRef) {}

  get mobileTopbarActive() {
    return this.layoutService.state.topbarMenuActive;
  }

  get appLogo(): string {
    return this.layoutService.config().appLogo;
  }

  get appName(): string {
    return this.layoutService.config().appName;
  }

  get visibleSideBar(): boolean {
    return this.layoutService.config().visibleSideBar;
  }

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
