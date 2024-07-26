import {Inject, Injectable} from "@angular/core";
import {ActiveToast, IndividualConfig, ToastrService} from "ngx-toastr";
import {GlobalConfig, MessageConfig, TOAST_CONFIG, ToastConfig, ToastSeverity} from "./toast.common";
import {Asserts, Callback, Objects} from "ts-ui/helper";
import {ToastItem} from "./toast-item";

const {isJson, isString, notBlank, notNull} = Objects;

export type ToastMessage = Partial<MessageConfig>;

function throwInvalidArgument(method: string, args: any[]) {
    throw new Error(`${method} invalid argument ${args.join(',')}`);
}

@Injectable({providedIn: 'root'})
export class ToastService {

    constructor(
        @Inject(TOAST_CONFIG)
        private globalConfig: ToastConfig,
        private service: ToastrService) {
    }

    /**
     * Show toast
     * @param message the message toast
     * */
    show(message: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param override custom message config
     * */
    show(message: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * */
    show(message: string, title: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * @param override custom message config
     * */
    show(message: string, title: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param config custom message config
     * */
    show(config: ToastMessage): ActiveToast<any>;

    /**@protected*/
    show(...args: any[]): ActiveToast<any> {
        return this.showInternal(args, 'show()');
    }

    /**
     * Show toast
     * @param message the message toast
     * */
    info(message: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param override custom message config
     * */
    info(message: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * */
    info(message: string, title: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * @param override custom message config
     * */
    info(message: string, title: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param config custom message config
     * */
    info(config: ToastMessage): ActiveToast<any>;

    /**@protected*/
    info(...args: any[]): ActiveToast<any> {
        return this.showInternal(args, 'info()', 'info');
    }

    /**
     * Show toast
     * @param message the message toast
     * */
    success(message: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param override custom message config
     * */
    success(message: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * */
    success(message: string, title: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * @param override custom message config
     * */
    success(message: string, title: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param config custom message config
     * */
    success(config: ToastMessage): ActiveToast<any>;

    /**@protected*/
    success(...args: any[]): ActiveToast<any> {
        return this.showInternal(args, 'success()', 'success');
    }

    /**
     * Show toast
     * @param message the message toast
     * */
    help(message: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param override custom message config
     * */
    help(message: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * */
    help(message: string, title: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * @param override custom message config
     * */
    help(message: string, title: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param config custom message config
     * */
    help(config: ToastMessage): ActiveToast<any>;

    /**@protected*/
    help(...args: any[]): ActiveToast<any> {
        return this.showInternal(args, 'help()', 'help');
    }

    /**
     * Show toast
     * @param message the message toast
     * */
    warning(message: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param override custom message config
     * */
    warning(message: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * */
    warning(message: string, title: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * @param override custom message config
     * */
    warning(message: string, title: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param config custom message config
     * */
    warning(config: ToastMessage): ActiveToast<any>;

    /**@protected*/
    warning(...args: any[]): ActiveToast<any> {
        return this.showInternal(args, 'warning()', 'warning');
    }

    /**
     * Show toast
     * @param message the message toast
     * */
    error(message: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param override custom message config
     * */
    error(message: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * */
    error(message: string, title: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * @param override custom message config
     * */
    error(message: string, title: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param config custom message config
     * */
    error(config: ToastMessage): ActiveToast<any>;

    /**@protected*/
    error(...args: any[]): ActiveToast<any> {
        return this.showInternal(args, 'error()', 'error');
    }

    /**
     * Show toast
     * @param message the message toast
     * */
    loading(message: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param override custom message config
     * */
    loading(message: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * */
    loading(message: string, title: string): ActiveToast<any>;

    /**
     * Show toast
     * @param message the message toast
     * @param title the title toast
     * @param override custom message config
     * */
    loading(message: string, title: string, override: ToastMessage): ActiveToast<any>;

    /**
     * Show toast
     * @param config custom message config
     * */
    loading(config: ToastMessage): ActiveToast<any>;

    /**@protected*/
    loading(...args: any[]): ActiveToast<any> {
        return this.showInternal(args, 'loading()', 'loading', cfg => {
            cfg.disableTimeOut=true;
            cfg.progressMode = 'indeterminate';
            //cfg.timeOut = 100000;
            return cfg;
        });
    }

    remove(activeToast: ActiveToast<any>): void {
        Asserts.notNull(activeToast, "@ActiveToast<any>");
        this.service.remove(activeToast.toastId);
    }

    close(activeToast: ActiveToast<any>): void {
        Asserts.notNull(activeToast, "@ActiveToast<any>");
        this.service.clear(activeToast.toastId);
    }

    closeAll(): void {
        this.service.clear();
    }

    /** create a clone of global config and apply individual settings */
    private applyConfig(override: Partial<GlobalConfig> = {}): MessageConfig {
        return Objects.mergeDeep({...this.globalConfig}, override);
    }

    private showInternal<Payload=any>(args: any[], method: string, severity?: ToastSeverity, customConfig?: Callback<ToastMessage, ToastMessage>): ActiveToast<Payload> {
        let config: ToastMessage = {}, message: string, title: string;
        let argLength = args.length;

        //(message: string)
        //(config: ConfigType)
        if (argLength === 1) {
            if (isString(args[0])) message = args[0];
            else if (isJson(args[0])) config = args[0];
            else throwInvalidArgument(method, args);
        }

        //(message: string, override: ConfigType)
        //(message: string, title: string)
        else if (argLength === 2) {
            if (isString(args[1])) {message = args[0];title = args[1];}
            else if (isJson(args[1])) {message = args[0];config = args[1];}
            else throwInvalidArgument(method, args);
        }

        //(message: string, title: string, override: ConfigType)
        else if (argLength === 3) {message = args[0];title = args[1];config = args[2];}

        // other -> error
        else throwInvalidArgument(method, args);

        //update config

        config = config ?? {};
        config = notNull(customConfig) ? customConfig(config) : config;
        config = this.applyConfig({title, message, ...config});
        config.severity = notBlank(severity) ? severity : config.severity;
        config.toastComponent = config.toastComponent ?? ToastItem;

        // toastr config
        const tConfig: Partial<IndividualConfig> = {...config};
        tConfig.positionClass =  `${config.toastClass}-${config.position ?? 'top-right'}`;
        tConfig.toastClass = `${config.toastClass}-message`;

        const type = tConfig.toastClass + '-' + (config.severity ?? 'info');
        return this.service.show(config.message, config.title, tConfig, type);
    }


}