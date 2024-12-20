import { OverlayContainer } from "@angular/cdk/overlay";
import { Injectable } from "@angular/core";

@Injectable({providedIn: 'root'})
export class CdkOverlayContainer extends OverlayContainer {

    override getContainerElement(): HTMLElement {
        return super.getContainerElement();
    }

    protected override _createContainer(): void {
        console.log(`_createContainer`);
        super._createContainer();
    }
}