import { CommonModule } from "@angular/common";
import { ChangeDetectionStrategy, Component, inject, Injectable, ViewEncapsulation } from "@angular/core";
import { BaseComponent, BaseComponentStyle } from "primeng/basecomponent";
import { theme } from "./userbutton.style";

@Injectable()
export class UserButtonStyle extends BaseComponentStyle {
    name = "userbutton";
    classes = { root: 'ts-user-button'};
    theme: any = theme;
}

@Component({
    standalone: true,
    selector: 'ts-user-button',
    templateUrl: './tag.component.html',
    styles: ` ts-user-button { display: block; }  `,
    imports: [CommonModule],
    providers: [UserButtonStyle],
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserButton extends BaseComponent {
    _component = inject(UserButtonStyle);
}