import { CommonModule } from "@angular/common";
import { booleanAttribute, Component, Input, numberAttribute, ViewEncapsulation } from "@angular/core";
import { InputTextModule } from "primeng/inputtext";
import { Objects } from "ts-ui/helper";

const {isTrue, notNull} = Objects;

@Component({
    selector: 'ts-input-group',
    imports: [CommonModule, InputTextModule],
    encapsulation: ViewEncapsulation.None,
    standalone: true,
    styleUrl: './prefix.scss',
    templateUrl: './input-group.html'
})
export class InputGroup {

    @Input({ transform: booleanAttribute })
    compact: boolean = false;

    @Input({transform: numberAttribute})
    addonGap: number;

    get containerClass(): any {
        return {
            [`input-group-compact`]: this.isCompact,
            [`gap-${this.addonGap}`]: !this.isCompact && notNull(this.addonGap)
        };
    }

    get addonClass(): any {
        return {
            [`gap-${this.addonGap}`]: !this.isCompact && notNull(this.addonGap)
        };
    }

    get isCompact(): boolean {
        return isTrue(this.compact);
    }
}