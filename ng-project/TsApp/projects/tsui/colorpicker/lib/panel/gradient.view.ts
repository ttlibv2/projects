import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Color, HsbaColorType } from '../color';
import { Utils } from '../utils';

@Component({
    selector: 'color-gradient-view',
    standalone: true,
    template: `
    <div
      class="ts-colorpicker-gradient"
      style="position: absolute; inset: 0"
      [style.background]="'linear-gradient(' + direction + ', ' + gradientColors + ')'">
      <ng-content></ng-content>
    </div>
  `
})
export class GradientView implements OnInit, OnChanges {
    @Input() colors: Color[] | string[] = [];
    @Input() direction: string = 'to right';
    @Input() type: HsbaColorType = 'hue';

    gradientColors: string = '';

    constructor() { }

    ngOnInit(): void {
        this.useMemo();
    }

    ngOnChanges(changes: SimpleChanges): void {
        const { colors, type } = changes;
        if (colors || type) {
            this.useMemo();
        }
    }

    useMemo(): void {
        this.gradientColors = this.colors
            .map((color, idx) => {
                const result = Utils.generateColor(color);
                if (this.type === 'alpha' && idx === this.colors.length - 1) {
                    result.setAlpha(1);
                }
                return result.toRgbString();
            })
            .join(',');
    }
}