import { Component, Directive } from "@angular/core";
import { BaseIcon } from "./base-icon";

@Component({
 //  standalone: true,
    selector: 'ts-icon',
  //  imports: [BaseIcon],
    template: ``, 
   // exportAs: 'font-icon',
    host: {
        '[class.icon]': 'true',
        '[style.display]': 'block',
    }
})
export class FontIcon extends BaseIcon { 


}