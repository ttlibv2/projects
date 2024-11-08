import { Component, EventEmitter, Input, Output } from '@angular/core';
import {Utils} from "../utils";

@Component({
  selector: 'color-block-view',
  standalone: true,
  template: `
    <div class="ts-colorpicker-color-block" (click)="onClick.emit(true)">
      <div class="ts-colorpicker-color-block-inner" [style.background-color]="color"></div>
    </div>
  `
})
export class BlockView {
  @Input() color: string = Utils.defaultColor.toHsbString();
  @Output() readonly onClick = new EventEmitter<boolean>();
}