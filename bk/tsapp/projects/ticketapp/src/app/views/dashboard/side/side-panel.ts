import { CommonModule } from "@angular/common";
import { ChangeDetectionStrategy, Component, ViewEncapsulation } from "@angular/core";

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CommonModule],
    styles: [`ts-side-panel {display: block}`],
    selector: 'ts-side-panel',
    templateUrl: './view/side-panel.html'
})
export class SidePanel {

}