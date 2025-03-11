import { inject, Injectable } from "@angular/core";
import { Alert } from "ts-ui/alert";
import { ToastService } from "ts-ui/toast";

@Injectable({ providedIn: 'root' })
export class DialogService {
    private _toast = inject(ToastService);
    private _alert = inject(Alert);


}