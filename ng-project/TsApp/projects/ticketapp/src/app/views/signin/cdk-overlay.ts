import { Overlay, OverlayConfig, OverlayContainer, OverlayRef } from "@angular/cdk/overlay";
import { DomPortalOutlet } from "@angular/cdk/portal";
import { DOCUMENT } from "@angular/common";
import { inject, Injectable } from "@angular/core";

export class CdkOverlayConfig extends OverlayConfig {
    appendTo?: any;

    constructor(config?: OverlayConfig) {
        super(config);
        if(config && 'appendTo' in config) {
            this.appendTo = config['appendTo'];
        }
    }

}

@Injectable({providedIn: 'root'})
export class CdkOverlay extends Overlay {
    document = inject(DOCUMENT);
    overlayContainer = inject(OverlayContainer);


    override create(config?: CdkOverlayConfig): OverlayRef {
        console.log(`CdkOverlay: create()`, config);
        this['_createHostElement'] = this.createHostElement;
        return super.create(config);
    }

    /**
     * Creates the host element that wraps around an overlay
     * and can be used for advanced positioning.
     * @returns Newly-create host element.
     */
    private createHostElement(): HTMLElement {
        console.log(`createHostElement`);
        const host = this.document.createElement('div');
        this.overlayContainer.getContainerElement().appendChild(host);
        return host;
    }

}