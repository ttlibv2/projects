import { DOCUMENT } from "@angular/common";
import { Inject, Injectable } from "@angular/core";


@Injectable({ providedIn: 'root' })
export class StyleManager {
    cssId: string = 'theme-link';

    constructor(@Inject(DOCUMENT)
    private document: Document) { }

    /**
      * Remove the stylesheet with the specified key.
      */
    removeStyle(key: string) {
        const existingLinkElement = this.getExistingLinkElementByKey(key);
        if (existingLinkElement) this.document.head.removeChild(existingLinkElement);
    }

    /**
     * Set the stylesheet with the specified key.
     */
    setStyle(key: string, href: string) {
        this.getLinkElementForKey(key).setAttribute('href', href);
    }


    private getLinkElementForKey(key: string) {
        return this.getExistingLinkElementByKey(key) || this.createLinkElementWithKey(key);
    }

    private getExistingLinkElementByKey(key: string) {
        return this.document.head.querySelector(`link[rel="stylesheet"].${this.getClassNameForKey(key)}`);
    }

    private createLinkElementWithKey(key: string) {
        const linkEl = this.document.createElement('link');
        linkEl.setAttribute('rel', 'stylesheet');
        linkEl.classList.add(this.getClassNameForKey(key));
        this.document.head.appendChild(linkEl);
        return linkEl;
    }

    private getClassNameForKey(key: string) {
        return `style-manager-${key}`;
    }


}