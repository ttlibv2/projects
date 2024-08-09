import { input, OnDestroy, effect, Renderer2, ElementRef, Directive, booleanAttribute, RendererStyleFlags2, Component } from "@angular/core";
import { Severity } from "ts-ui/common";
import { Objects } from "ts-ui/helper";
const { notNull, isBlank, notBlank } = Objects;

export function asBoolean(value: any): boolean {
    return booleanAttribute(value);
}

interface ObjectIcon {
    [key: string]: any;
    icon?: string;
    color?: string;
    spin?: boolean;
    spinClass?: string;
    size?: string;
    severity?: Severity;
}

@Component({
    template: `<ng-content></ng-content>`
})
export class BaseIcon implements OnDestroy {

    /** Define the icon */
    readonly icon = input<string>();

    /** Define the color of the icon */
    readonly color = input<string>();

    /** Define the size of the icon */
    readonly size = input<string>('1rem');

    /** Define the spin of the icon */
    readonly spin = input<boolean, boolean>(false, {transform: value => asBoolean(value)});

    /** Define the spin class of the icon */
    readonly spinClass = input<string>('pi-spin');

    /** Define the severity of the icon */
    readonly severity = input<Severity>();

    // 
    private object: ObjectIcon = {};

    constructor(private renderer: Renderer2,
        private elementRef: ElementRef) {
        effect(() => this.updateIcon());
    }

    updateIcon() {
        this.handleSpin();
        this.handleIcon();
        this.handleSeverity();
        this.handleColor();
        this.handleSize();
    }

    ngOnDestroy(): void { }


    protected handleSpin(): void {
        const newSpin = this.spin(), newCl = this.spinClass();
        const {spinClass} = this.object;

        if(newSpin === false) {
            this.addRemoveClass(null, this.object.spinClass);
            this.object.spin = false;
        }
        else if(spinClass !== newCl) {
            this.object.spin = true;
            this.object.spinClass = newCl;
            this.addRemoveClass(newCl, spinClass);
        }
    }

    protected handleIcon() {
        const newIcon = this.icon(), oldIcon = this.object.icon;
        if(newIcon !== oldIcon){
            this.object.icon = newIcon;
            this.addRemoveClass(newIcon, oldIcon);            
        }
    }

    protected handleColor() {
        const newColor = this.color(), oldColor = this.object.color;
        if(newColor !== oldColor){
            this.object.color = newColor;
            this.addRemoveStyle('color', newColor, oldColor);            
        }
    }

    protected handleSize() {
        const newSize = this.size(), oldSize = this.object.size;
        if(newSize !== oldSize){
            this.object.size = newSize;
            this.addRemoveStyle('font-size', newSize, oldSize);            
        }
    }

    protected handleSeverity() {
        const newSeverity = this.severity(), oldSeverity = this.object.severity;
        const clzz = isBlank(newSeverity) ? null : `icon-${newSeverity}`;
        if(newSeverity !== oldSeverity){
            this.addRemoveClass(clzz, oldSeverity);
            this.object.severity = newSeverity;
        }
    }




    protected addRemoveClass(newClass: string, oldClass: string, hasAdd: boolean = true) {
        if (hasAdd === true && oldClass !== newClass) {
            const el = this.elementRef.nativeElement;

            if (notNull(newClass)) {
                newClass.split(' ').forEach(cl => this.renderer.addClass(el, cl)) ;
            }

            if (notNull(oldClass)){ 
                oldClass.split(' ').forEach(cl => this.renderer.removeClass(el, cl)) ;
            }
        }
    }

    protected addRemoveStyle(name: string, newStyle: string, oldStyle: string) {        
        const el = this.elementRef.nativeElement;
        if(oldStyle !== newStyle) {
            if(notBlank(newStyle))this.renderer.setStyle(el, name, newStyle);
            if(notBlank(oldStyle))this.renderer.removeStyle(el, name);
        }



    }




}