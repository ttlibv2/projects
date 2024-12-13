import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Utils } from '../utils';
import { Color } from '../color';

@Component({
  selector: 'color-block-view',
  standalone: true,
  template: `
    <div class="ts-colorpicker-block" (click)="onClick.emit(true)">
      <div class="ts-colorpicker-block-inner" [style.background-color]="bgColor"></div>
    </div>
  `
})
export class BlockView implements OnInit {
  @Input() color: Color | string = Utils.defaultColor;
  @Output() onClick = new EventEmitter<boolean>();

  get bgColor(): string {
    return this.color instanceof Color ? this.color.toRgbString(): this.color;
  }

  ngOnInit(): void {
    console.log(`BlockView`, this.color)
  }
}