import { Injectable, OnDestroy, Pipe, PipeTransform } from "@angular/core";
import { TranslatePipe } from "@ngx-translate/core";
import { Objects } from "ts-ui/helper";

@Injectable()
@Pipe({
    standalone: true,
    name: 'i18n',
    pure: false // required to update the value when the promise is resolved
})
export class I18NPipe extends TranslatePipe {

    override transform(value: any, ...args: any[]) {
        const newValue = Objects.parseI18N(value);
        return Objects.isBlank(newValue) ? value : super.transform(newValue, ...args);
    }

}