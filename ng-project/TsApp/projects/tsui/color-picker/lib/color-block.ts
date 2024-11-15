
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { BlockView } from './panel/block.view';
import { Utils } from './utils';
import { Color } from './color';
const {defaultColor} = Utils;

@Component({
    standalone: true,
    selector: 'ts-color-block',    
    encapsulation: ViewEncapsulation.None,
    imports: [BlockView],
    template: ` <color-block-view [color]="color" (onClick)="onClick.emit($event)" />`,
    host: {
        'class': 'ts-colorpicker-inline',
        '[class.ts-colorpicker-inline-sm]': `nzSize === 'small'`,
        '[class.ts-colorpicker-inline-lg]': `nzSize === 'large'`
    }
})
export class ColorBlock implements OnChanges {
    @Input() color: Color = defaultColor;
    @Input() size: 'small' | 'larger' | 'default' = 'default';
    @Output() onClick = new EventEmitter<boolean>();

    constructor() { }

    ngOnChanges(changes: SimpleChanges): void {
        // console.log(changes)
    }
}