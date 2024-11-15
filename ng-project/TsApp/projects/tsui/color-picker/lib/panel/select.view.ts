
import { AfterViewInit, Component, OnChanges, OnInit, ViewEncapsulation } from '@angular/core';
import { ColorHandler } from './handler.view';
import { ColorPalette } from './palette.view';
import { DragView } from './drag';





@Component({
  standalone: true,
  selector: 'color-select-view',
  imports: [ColorHandler, ColorPalette],
  encapsulation: ViewEncapsulation.None,
  template: `
    <div  #slider  class="ts-colorpicker-select"
      (mousedown)="dragStartHandle($event)"
      (touchstart)="dragStartHandle($event)">
      <color-palette-view> 
        <div #transform
          style="position: absolute; z-index: 1;"
          [style.left]="offsetValue.x + 'px'"
          [style.top]="offsetValue.y + 'px'">
          <color-handler-view [colorRgb]="toRgbString()" />
        </div>
        <div
          class="ts-colorpicker-saturation" [style.background-color]="toHsb()"
          style=" background-image: linear-gradient(0deg, #000, transparent),  linear-gradient(90deg, #fff, hsla(0, 0%, 100%, 0));" ></div>
      </color-palette-view>
    </div>
  `
})
export class SelectView extends DragView implements OnInit, AfterViewInit, OnChanges {

  toRgbString(): string {
    return undefined;//this.color?.toRgbString() as string;
  }

  toHsb(): string {
    return `hsl(${this.color?.toHsb().h},100%, 50%)`;
  }


}