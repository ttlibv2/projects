import {ChangeDetectionStrategy, Component, ElementRef, inject, Input, OnChanges, OnInit, Renderer2, SimpleChanges, TemplateRef, ViewEncapsulation} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AnyTemplateOutlet} from "ts-ui/common";

export const dividerPrefix = 'ts-divider';

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
  @Input() align: 'left' | 'right' | 'top' | 'center' | 'bottom';

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
   * color line
   * @group Props
   */  
  @Input() text: string | TemplateRef<any>;

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
  private elementRef = inject(ElementRef);

  ngOnInit(): void {
    
  }

  ngOnChanges(changes: SimpleChanges): void {
    const {color, size, type} = changes;
    const el = this.elementRef.nativeElement;
    if(color) this.renderer.setStyle(el, '--divider-color', this.color);
    if (size) this.renderer.setStyle(el, '--divider-size', this.size);
    if (type) this.renderer.setStyle(el, '--divider-type', this.type);
  }
}