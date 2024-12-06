import {ChangeDetectionStrategy, Component, ElementRef, inject, Input, numberAttribute, OnChanges, OnInit, Renderer2, SimpleChanges, TemplateRef, ViewEncapsulation} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AnyTemplateOutlet, DomHandler, Severity, StringTemplate} from "ts-ui/common";
import { Objects } from "ts-ui/helper";

export const dividerPrefix = 'ts-divider';
const {isNull} = Objects;

@Component({
  standalone: true,
  selector: 'ts-divider',
  exportAs: 'tsDivider',
  preserveWhitespaces: false,
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, AnyTemplateOutlet],
  template: ` 
        @if(text) {
            <span class="ts-divider-content">
              <ng-container *anyTemplate="text">
                <span [innerHTML]="text"></span>
              </ng-container>
        </span>
        }
  `,
  host: {
    '[class.p-component]': 'true',
    '[class.ts-divider]': 'true',
    '[class.ts-divider-horizontal]': `!isVertical`,
    '[class.ts-divider-vertical]': `isVertical`,
    '[class.ts-divider-solid]': `type === 'solid'`,
    '[class.ts-divider-dashed]': `type === 'dashed'`,
    '[class.ts-divider-dotted]': `type === 'dotted'`,
    '[class.ts-divider-top]': `isVertical && align === 'top'`,
    '[class.ts-divider-bottom]': `isVertical && align === 'bottom'`,
    '[class.ts-divider-left]': `isLeft`,
    '[class.ts-divider-center]': `isCenter`,
    '[class.ts-divider-right]': `isRight`,
    '[class.ts-divider-with-text]': `text`,
    '[class.ts-divider-with-text-left]': `text && isLeft`,
    '[class.ts-divider-with-text-right]': `text && isRight`,
    '[class.ts-divider-with-text-center]': `text && isCenter`,
    '[class.pc-0]': `percent === 0`
  }
})
export class Divider implements OnChanges, OnInit {

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
  @Input() align: 'left' | 'right' | 'top' | 'center' | 'bottom' | 'first' | 'last';

  /**
   * severity
   * @group Props
   */
  @Input() severity: Severity;

  /**
   * width | height line
   * @group Props
   */
  @Input() size: string;

  /**
   * color line
   * @group Props
   */
  @Input() color: string;

  /**
   * percent line
   * @group Props
   */
  @Input({transform: numberAttribute}) percent: number;

  /**
   * color line
   * @group Props
   */  
  @Input() text: StringTemplate;

  get isTemplate(): boolean {
    return this.text instanceof TemplateRef;
  }

  get isLeft(): boolean {
    return !this.isVertical && this.align === 'left';
  }

  get isRight(): boolean {
    return !this.isVertical && this.align === 'right';
  }

  get isCenter(): boolean {
    return this.align === 'center';
  }

  get isVertical(): boolean {
    return this.layout === 'vertical';
  }

  private renderer = inject(Renderer2);
  private elementRef: ElementRef<HTMLElement> = inject(ElementRef);

  ngOnInit(): void {
    
  }

  ngOnChanges(changes: SimpleChanges): void {
    const {color, size, type, percent, severity} = changes;
    if(color) this.setStyle('--divider-color', this.color);
    if (size) this.setStyle( '--divider-size', this.size);
    if (type) this.setStyle( '--divider-type', this.type);
    if (percent) {
      const val = isNull(this.percent) ? undefined : `${this.percent}%`;
      this.setStyle( '--divider-percent', val);    
    }

    if(severity) {
      let {previousValue: prev, currentValue: curr} = severity;
      let element = this.elementRef.nativeElement;
      let func = (name: Severity) => `ts-divider-${name}`;
      if (!!prev) DomHandler.removeClass(element, func(prev));
      if (!!curr) DomHandler.addClass(element, func(curr));
    }

  }

  private setStyle(property: string, value: any): void {
    this.elementRef.nativeElement.style.setProperty(property, value);

  }
}