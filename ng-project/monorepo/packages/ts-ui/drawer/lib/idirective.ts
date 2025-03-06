import { Directive, TemplateRef } from "@angular/core";

@Directive({
    standalone: true,
    selector: '[tsDrawerHeader]'
})
export class DrawerHeader {

    constructor(public template: TemplateRef<any>) {}
}