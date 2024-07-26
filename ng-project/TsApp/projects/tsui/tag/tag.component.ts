import { AfterContentInit, booleanAttribute, ChangeDetectionStrategy, Component, ContentChildren, ElementRef, EventEmitter, Input, Output, QueryList, Renderer2, TemplateRef, ViewEncapsulation } from '@angular/core';
import { ImageErrorEvent, TagMode, TagRemoveEvent } from './tag.common';
import { Objects } from 'ts-ui/helper';
import { PrimeTemplate } from 'primeng/api';

const { notBlank, notNull } = Objects;


@Component({
  selector: 'ts-tag',
  templateUrl: './tag.component.html',
  styles: ` :host { display: block; }  `, 
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '(click)': `updateCheckedStatus()`
  }
})
export class TagComponent implements AfterContentInit {

  @Input() mode: TagMode = 'default';
  @Input() color: string;

  /**
   * Defines the icon to display.
   * @group Props
   */
  @Input() icon: string;

  /**
   * Defines the image to display.
   * @group Props
   */
  @Input() image: string | undefined;

  // @Input() imagePos: 'left' | 'right' = 'left';

  /**
   * Alt attribute of the image.
   * @group Props
   */
  @Input() alt: string | undefined;

  /**
  * Inline style of the element.
  * @group Props
  */
  @Input() style: { [klass: string]: any } | null | undefined;
  /**
   * Class of the element.
   * @group Props
   */
  @Input() styleClass: string | undefined;

  /**
    * Defines the style of the button.
    * @group Props
    */
  @Input() severity: 'success' | 'info' | 'warning' | 'danger' | 'help' | 'primary' | 'secondary' | 'contrast' | undefined;

  @Input() size: string;
  @Input() value: any;

  /**
    * Defines the text to display.
    * @group Props
    */
  @Input() label: string | ((value: any) => string);

  /**
      * Icon of the remove element.
      * @group Props
      */
  @Input() removeIcon: string = 'pi pi-times';

  @Input() removeAriaLabel: string = 'Remove';
 
  @Input({ transform: booleanAttribute }) bordered: boolean = false;
  @Input({ transform: booleanAttribute }) raised: boolean = false;
  @Input({ transform: booleanAttribute }) rounded: boolean = false;
  @Input({ transform: booleanAttribute }) disabled: boolean = false;
  @Input({ transform: booleanAttribute }) text: boolean = false;
  @Input({ transform: booleanAttribute }) outlined: boolean = false;
  @Input({ transform: booleanAttribute }) checked: boolean = false;

  /**
    * Whether to display a remove icon.
    * @group Props
    */
  @Input({ transform: booleanAttribute }) removable: boolean = false;


  /**
   * Callback to invoke when a chip is removed.
   * @param {MouseEvent} event - Mouse event.
   * @group Emits
   */
  @Output() onRemove = new EventEmitter<TagRemoveEvent>();

  @Output()
  onChecked = new EventEmitter<any>();

  /**
    * This event is triggered if an error occurs while loading an image file.
    * @param {Event} event - Browser event.
    * @group Emits
    */
  @Output() onImageError = new EventEmitter<ImageErrorEvent>();

  @ContentChildren(PrimeTemplate) 
  templates: QueryList<PrimeTemplate>;

  removeIconTemplate: TemplateRef<any> | undefined;
  visible: boolean = true;

  get ariaLabel(): string {
    if (this.label === undefined) return undefined;
    else if (typeof this.label === 'string') return this.label;
    else return this.value !== undefined && typeof this.label === 'function' ? this.label(this.value) : undefined;
  }

  get labelView(): string {
    return this.label === undefined ? undefined
      : typeof this.label === 'string' ? this.label 
        : this.value && this.label(this.value);
  }

  get uiStyle(): any {
    return {
      ...this.style,
      //  'background-color': this.presetColor ? '' : this.color,
    }
  }

  constructor(private renderer: Renderer2,
    private element: ElementRef) {}

  ngAfterContentInit(): void {
    this.templates?.forEach(item => {
      switch (item.getType()) {
        default:
            this.removeIconTemplate = item.template;
            break;
    }
    });
  }

  imageError(event: any) {
    this.onImageError.emit({ event, image: this.image });
  }


  handleRemove(event: MouseEvent) {
    this.onRemove.emit({ value: this.value, event });
    if(!event.defaultPrevented) {
      const parentNode = this.renderer.parentNode(this.element.nativeElement);
      if(notNull(parentNode)) this.renderer.removeChild(parentNode, this.element.nativeElement);
    }
  }

  onKeydown(event: any) {
    if (event.key === 'Enter' || event.key === 'Backspace') {
      this.handleRemove(event);
    }
  }

  updateCheckedStatus(): void {
    console.log(`updateCheckedStatus`);
    if (this.mode === 'checkable') {
      this.checked = !this.checked;
      this.onChecked.emit({checked: this.checked, value: this.value});
    }
  }


  containerClass(): any {
    return {
      'ts-tag p-component': true,
      'ts-tag-image': notBlank(this.image),
      'ts-tag-checkable': this.mode === 'checkable',
      'ts-tag-checkable-checked': this.mode === 'checkable' && this.checked === true,
      'ts-tag-closeable': this.mode === 'closeable',
      'ts-tag-borderless': this.bordered === true,
      ['ts-tag-rounded']: this.rounded === true,
      ['ts-tag-raised']: this.raised === true,
      ['ts-tag-text']: this.text === true,
      ['ts-tag-outlined']: this.outlined === true,
      ['ts-tag-' + this.severity]: notBlank(this.severity),
      //'ts-tag-magenta': true
    };
  }


}