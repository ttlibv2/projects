import {SafeUrl} from "@angular/platform-browser";
import {ElementRef, TemplateRef} from "@angular/core";
import {INgClass, INgStyle} from "./interface";

export interface ImageObject {
  /**
   * Style class of the image element.
   * @group Props
   */
  imageClass?: INgClass;

  /**
   * Inline style of the image element.
   * @group Props
   */
  imageStyle?: INgStyle;
  /**
   * Class of the element.
   * @group Props
   */
  styleClass?: INgClass;

  /**
   * Inline style of the element.
   * @group Props
   */
  style?: INgStyle;

  /**
   * The source path for the main image.
   * @group Props
   */
  src?: string | SafeUrl | undefined;
  /**
   * The srcset definition for the main image.
   * @group Props
   */
  srcSet?: string | SafeUrl | undefined;
  /**
   * The sizes definition for the main image.
   * @group Props
   */
  sizes?: string | undefined;
  /**
   * The source path for the preview image.
   * @group Props
   */
  previewImageSrc?: string | SafeUrl | undefined;
  /**
   * The srcset definition for the preview image.
   * @group Props
   */
  previewImageSrcSet?: string | SafeUrl | undefined;
  /**
   * The sizes definition for the preview image.
   * @group Props
   */
  previewImageSizes?: string | undefined;
  /**
   * Attribute of the preview image element.
   * @group Props
   */
  alt?: string | undefined;
  /**
   * Attribute of the image element.
   * @group Props
   */
  width?: string | undefined;
  /**
   * Attribute of the image element.
   * @group Props
   */
  height?: string | undefined;
  /**
   * Attribute of the image element.
   * @group Props
   */
  loading?: 'lazy' | 'eager' | undefined;
  /**
   * Target element to attach the dialog, valid values are "body" or a local ng-template variable of another element (note?: use binding with brackets for template variables, e.g. [appendTo]="mydiv" for a div element having #mydiv as variable name).
   * @group Props
   */
  appendTo?: HTMLElement | ElementRef | TemplateRef<any> | string | null | undefined | any;
  /**
   * Controls the preview functionality.
   * @group Props
   */
  preview?: boolean;
  /**
   * Transition options of the show animation. Default?: '150ms cubic-bezier(0, 0, 0.2, 1)';
   * @group Props
   */
  showTransitionOptions?: string;//'150ms cubic-bezier(0, 0, 0.2, 1)';
  /**
   * Transition options of the hide animation. Default?: '150ms cubic-bezier(0, 0, 0.2, 1)'
   * @group Props
   */
  hideTransitionOptions?: string;// '150ms cubic-bezier(0, 0, 0.2, 1)';
  /**
   * Triggered when the preview overlay is shown.
   * @group Emits
   */
  onShow?: () => void;
  /**
   * Triggered when the preview overlay is hidden.
   * @group Emits
   */
  onHide?: () => void;
  /**
   * This event is triggered if an error occurs while loading an image file.
   * @param {Event} event - Browser event.
   * @group Emits
   */
  onImageError?: (event: Event) => void;
}