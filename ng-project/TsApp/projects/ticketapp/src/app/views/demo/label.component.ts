import {booleanAttribute, Component, HostBinding, Input, OnInit} from '@angular/core';
import {TooltipOptions} from "primeng/api";

@Component({
  selector: 'ts-label',
  templateUrl: './label.html'
})
export class LabelComponent implements OnInit {
  @Input() for?: string;
  @Input() text: string;

  @Input({transform: booleanAttribute})
  required: boolean = false;

  @Input({transform: booleanAttribute})
  visibleColon: boolean = true;

  @Input() requiredPos: 'begin' | 'end' = 'end';

  @Input({transform: booleanAttribute})
  visibleTooltip: boolean = true;

  @Input() tooltip: string;
  @Input() tooltipIcon: string = 'pi pi-question-circle';
  @Input() tooltipOptions: TooltipOptions = {};

  ngOnInit() {

  }

  get containerClass() {
    return {
      ['required-'+this.requiredPos]: true
    };
  }

}