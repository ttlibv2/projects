import {Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges,Renderer2,ElementRef, TemplateRef, ViewEncapsulation} from '@angular/core';
import {CommonModule} from "@angular/common";
import {PropCls, StyleCls, ImageObject, AnyTemplateOutlet} from "ts-ui/common";
import {Objects} from "ts-ui/helper";
import {ImageModule} from "primeng/image";
import {ButtonDirective, ButtonProps} from "primeng/button";
import {Router} from "@angular/router";

const {isNumber, isString, isTemplateRef, isNull} = Objects;

export type StatusHeader = string | ImageObject | TemplateRef<any>;

 
@Component({
  standalone: true,
  selector: 'ts-page-status',
  imports: [CommonModule, ImageModule, ButtonDirective, AnyTemplateOutlet],
  encapsulation: ViewEncapsulation.None,
  templateUrl: './status.component.html'
})
export class StatusPageView implements OnChanges, OnInit {
  isTemplateRef = isTemplateRef;

  /**
   * @group Props
   * */  
  @Input() color: string;

  /**
   * @group Props
   * */
  @Input() styleClass: PropCls;

  /**
   * @group Props
   * */
  @Input() style: StyleCls;

  /**
   * @group Props
   * */
  @Input() header: StatusHeader;

  /**
   * @group Props
   * */
  @Input() status: number | TemplateRef<any>;

  /**
   * @group Props
   * */
  @Input() title: string | TemplateRef<any>;

  /**
   * @group Props
   * */
  @Input() description: string | TemplateRef<any>;

  /**
   * @group Props
   * */
  @Input() homeButton: string | ButtonProps | TemplateRef<any>  = 'Back to home page';

  @Input() homeUrl: string | any[];

  @Output() onClickHome = new EventEmitter<any>();

  //--------
  headerObject: ImageObject;
  homeObject: ButtonProps;
  router = inject(Router);
  renderer = inject(Renderer2);
  elementRef = inject(ElementRef);

  ngOnInit() {
    this.homeObject = this.extractHomeObject();
  }

  ngOnChanges(changes: SimpleChanges) {
    const {header, homeButton, color} = changes;
    if(header) this.headerObject = this.extractHeaderObject();
    if(homeButton) this.homeObject = this.extractHomeObject();
    if(color) {
      const el = this.elementRef.nativeElement;
      this.renderer.setStyle(el, '--error-color', this.color);
    }
  }

  onShowHeaderImage(): void {
    const {onShow} = this.headerObject;
    if(onShow) onShow();
  }

  onHideHeaderImage(): void {
    const {onHide} = this.headerObject;
    if(onHide) onHide();
  }

  onHeaderImageError(event: any): void {
    const {onImageError} = this.headerObject;
    if(onImageError) onImageError(event);
  }

  onClickHomeBtn(event: any) {
    if(this.homeUrl && this.router) {
      const url: any = isString(this.homeUrl) ? [this.homeUrl] : this.homeUrl;
      this.router.navigate(url).then();
    }
    this.onClickHome.emit({event, url: this.homeUrl});
  }


  get pageCls(): any {
    return {
      [`page-status-s${this.status}`]: isNumber(this.status)
    };
  }

  private extractHeaderObject(): ImageObject | undefined {
    return isString(this.header) ? {src: this.header, 
      loading: 'lazy', width: '100px', height: '100px'}
      
      : isTemplateRef(this.header) ? undefined : this.header;
  }

  private extractHomeObject(): ButtonProps {
    return isNull(this.homeButton) || isTemplateRef(this.homeButton) ? undefined
      : isString(this.homeButton) ? {label: this.homeButton?.toUpperCase()} : this.homeButton;
  }
}