import { ChangeDetectionStrategy, Component, ViewEncapsulation } from "@angular/core";

@Component({
    standalone: true,  
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    selector: 'ts-drawer-container',
    exportAs: 'tsDrawerContainer',
    styleUrl: `drawer.scss`,
    templateUrl: 'drawer-container.html',
    host: {
        'class': 'ts-drawer-container'
    }
})
export class DrawerContainer {}