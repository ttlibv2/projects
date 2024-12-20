import { Platform } from "@angular/cdk/platform";
import { CdkScrollable } from "@angular/cdk/scrolling";
import { AfterContentInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, ViewEncapsulation } from "@angular/core";
import { DrawerContainer } from "./drawer-container";
import { CommonModule } from "@angular/common";


@Component({
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule],
    providers: [{ provide: CdkScrollable, useExisting: DrawerContent }],
    selector: 'ts-drawer-content',
    template: '<ng-content></ng-content>',
    host: {
        'class': 'ts-drawer-content',
        '[class.ts-drawer-content-hidden]': 'shouldBeHidden()',
        '[style.margin-left.px]': 'container.contentMargins.left',
        '[style.margin-right.px]': 'container.contentMargins.right',
        '[style.margin-top.px]': 'container.contentMargins.top',
        '[style.margin-bottom.px]': 'container.contentMargins.bottom',
    }
})
export class DrawerContent extends CdkScrollable implements AfterContentInit {
    readonly platform = inject(Platform);
    readonly changeDetectorRef = inject(ChangeDetectorRef);
    readonly container = inject(DrawerContainer);

    ngAfterContentInit() {
        this.container.contentMarginChanges.subscribe(() => {
            this.changeDetectorRef.markForCheck();
        });
    }

    /** Determines whether the content element should be hidden from the user. */
    protected shouldBeHidden(): boolean {
        // In some modes the content is pushed based on the width of the opened sidenavs, however on
        // the server we can't measure the sidenav so the margin is always zero. This can cause the
        // content to jump around when it's rendered on the server and hydrated on the client. We
        // avoid it by hiding the content on the initial render and then showing it once the sidenav
        // has been measured on the client.
        if (this.platform.isBrowser) {
            return false;
        }

        const { left: start, right: end } = this.container;
        return (
            (start != null && start.mode !== 'over' && start.isOpen) ||
            (end != null && end.mode !== 'over' && end.isOpen)
        );
    }
}