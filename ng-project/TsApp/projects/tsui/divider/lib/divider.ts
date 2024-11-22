import {ChangeDetectionStrategy, Component, ElementRef, Input, TemplateRef, ViewChild, ViewEncapsulation} from "@angular/core";
import {CommonModule} from "@angular/common";
import {PropCls} from "ts-ui/common";

export const dividerPrefix = 'ts-divider';

@Component({
  standalone: true,
  selector: 'ts-divider',
  exportAs: 'tsDivider',
  preserveWhitespaces: false,
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule],
  template: ` 
      <div class="ts-divider" [ngClass]="containerCls()" [ngStyle]="containerStyle()"  #container>
         
        @if(text) {
            <span class="ts-divider-content">
            @if(isTemplate) { <ng-container *ngTemplateOutlet="$any(text)"></ng-container> }
            @else { <span [innerHTML]="text"></span> }
                  </span>
        }
       
      </div>
  `,
})
export class Divider {

  /**
   * Inline style of the component.
   * @group Props
   */
  @Input() style: { [klass: string]: any };
  /**
   * Style class of the component.
   * @group Props
   */
  @Input() styleClass: PropCls;
  /**
   * Specifies the orientation.
   * @group Props
   */
  @Input() layout: 'horizontal' | 'vertical' = 'horizontal';
  /**
   * Border style type.
   * @group Props
   */
  @Input() type: 'solid' | 'dashed' | 'dotted' = 'solid';
  /**
   * Alignment of the content.
   * @group Props
   */
  @Input() align: 'left' | 'right' | 'top' | 'center' | 'bottom';
  /**
   * width | height line
   * @group Props
   */
  @Input() size: string = '2px';
  /**
   * color line
   * @group Props
   */
  @Input() color: string;

  @Input() text: string | TemplateRef<any>;

  @ViewChild('container', {static: true})
  container: ElementRef<HTMLElement>;

  get isTemplate(): boolean {
    return this.text instanceof TemplateRef;
  }

  get isLeft(): boolean {
    return this.layout === 'horizontal' && (!this.align || this.align === 'left');
  }

  get isRight(): boolean {
    return this.layout === 'horizontal' && this.align === 'right';
  }

  get isCenter(): boolean {
    return (this.layout === 'horizontal' && this.align === 'center') || (this.layout === 'vertical' && (!this.align || this.align === 'center'));
  }


  containerCls(): any {
    return {
      'ts-divider p-component': true,
      'ts-divider-horizontal': this.layout === 'horizontal',
      'ts-divider-vertical': this.layout === 'vertical',
      'ts-divider-solid': this.type === 'solid',
      'ts-divider-dashed': this.type === 'dashed',
      'ts-divider-dotted': this.type === 'dotted',
      'ts-divider-top': this.layout === 'vertical' && this.align === 'top',
      'ts-divider-bottom': this.layout === 'vertical' && this.align === 'bottom',
      'ts-divider-left': this.isLeft,
      'ts-divider-center': this.isCenter,
      'ts-divider-right': this.isRight,
      'ts-divider-with-text': this.text,
      'ts-divider-with-text-left': this.text && this.isLeft,
      'ts-divider-with-text-right': this.text &&this.isRight,
      'ts-divider-with-text-center': this.text &&this.isCenter,
    };
  }

  containerStyle(): any {
    return {
      ...this.style,
      [`--divider-color`]: this.color,
      [`--divider-size`]: this.size,
      [`--divider-style`]: this.type,
    };
  }
}