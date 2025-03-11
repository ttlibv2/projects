
import { Component, Input } from '@angular/core';

type HandlerSize = 'default' | 'small';

@Component({
  selector: 'color-handler-view',
    standalone: true,
    template: `
    <div
      class="ts-colorpicker-handler"
      [style.background-color]="colorRgb"
      [class.ts-colorpicker-handler-sm]="size === 'small'"></div>
  `
})
export class ColorHandler {
    @Input() colorRgb: string | null = null;
    @Input() size: HandlerSize = 'default';

}