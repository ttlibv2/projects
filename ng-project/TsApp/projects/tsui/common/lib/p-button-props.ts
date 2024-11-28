import { Directive, Input, OnChanges, SimpleChanges } from "@angular/core";
import {Severity} from "./interface";
import { ButtonDirective } from "primeng/button";

export interface ButtonProps {
  /**
   * Position of the icon.
   * @group Props
   */
  iconPos?: 'left' | 'right' | 'top' | 'bottom';

  /**
   * Uses to pass attributes to the loading icon's DOM element.
   * @group Props
   */
  loadingIcon?: string | undefined;
  /**
   * Text of the button.
   * @group Props
   */
  label?: string | undefined;

  /**
   * Name of the icon.
   * @group Props
   */
  icon?: string;

  /**
   * Whether the button is in loading state.
   * @group Props
   */
  loading?: boolean;

  /**
   * Defines the style of the button.
   * @group Props
   */
  severity?: Severity;
  /**
   * Add a shadow to indicate elevation.
   * @group Props
   */
  raised?: boolean;
  /**
   * Add a circular border radius to the button.
   * @group Props
   */
  rounded?: boolean;
  /**
   * Add a textual class to the button without a background initially.
   * @group Props
   */
  text?: boolean;
  /**
   * Add a border class without a background initially.
   * @group Props
   */
  outlined?: boolean;
  /**
   * Defines the size of the button.
   * @group Props
   */
  size?: 'small' | 'large' | undefined | null;
  /**
   * Add a plain textual class to the button without a background initially.
   * @group Props
   */
  plain?: boolean;
}

@Directive({
  standalone: true, 
  selector: '[pButtonCustom]',
  exportAs: 'pButtonCustom'
})
export class pButtonCustom implements OnChanges {
  @Input() props: ButtonProps;

  ngOnChanges(changes: SimpleChanges): void {
    
  }

}