import { Component, ElementRef, ViewChild } from '@angular/core';
import { LayoutService } from '../services/layout.service';

@Component({
  selector: 'ts-app-sidebar',
  templateUrl: './app-sidebar.component.html'
})
export class AppSidebarComponent {
  timeout: any = null;

  get anchored(): boolean {
    return this.layoutService.state.anchored;
  }

  get menuProfilePosition() {
    return this.layoutService.config().menuProfilePosition
  }

  @ViewChild('menuContainer')
  menuContainer: any;

  constructor(
    public layoutService: LayoutService, 
    public el: ElementRef) {
  }

  onMouseEnter() {
    console.log(`onMouseEnter`);
    this.layoutService.state.sidebarActive = true;

    if(this.anchored ) {
      if(this.timeout) {
        clearTimeout(this.timeout);
      this.timeout = null;
      }
    }
  }

  onMouseLeave() {
    if(this.anchor || this.timeout) {
      this.timeout = setTimeout(() => this.layoutService.state.sidebarActive = false, 300);
    }
  }

  anchor() {
    this.layoutService.toggleAnchor();
  }

  resetOverlay() {
    if(this.layoutService.state.overlayMenuActive) {
      this.layoutService.state.overlayMenuActive = false;
    }
  }



  ngOnDestroy() {
    this.resetOverlay()
  }

}
