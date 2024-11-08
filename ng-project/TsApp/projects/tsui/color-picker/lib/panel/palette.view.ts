import { Component } from '@angular/core';

@Component({
    selector: 'color-palette-view',
    standalone: true,
    template: `
    <div class="ts-colorpicker-palette" style="position: relative">
      <ng-content></ng-content>
    </div>
  `
})
export class ColorPalette { }