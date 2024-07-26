import {animate, state, style, transition, trigger} from '@angular/animations';
import {
    ChangeDetectionStrategy,
    Component,
    HostBinding,
    HostListener,
    NgZone,
    OnDestroy,
    SecurityContext,
    signal,
    ViewEncapsulation,
    WritableSignal,
} from '@angular/core';
import {Subscription} from 'rxjs';
import {MessageConfig} from './toast.common';
import {ToastPackage, ToastrService} from 'ngx-toastr';
import {Objects} from "ts-ui/helper";
import {DomSanitizer} from "@angular/platform-browser";
import {CommonModule} from "@angular/common";
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";
import {ProgressBarModule} from "primeng/progressbar";

const {notBlank, notNull} = Objects;

@Component({
    selector: '[toast-item]',
    templateUrl: './toast-item.html',
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, ButtonModule, RippleModule, ProgressBarModule],
    animations: [
        trigger('flyInOut', [
            state('inactive', style({ opacity: 0 })),
            state('active', style({ opacity: 1 })),
            state('removed', style({ opacity: 0 })),
            transition('inactive => active', animate('{{ easeTime }}ms {{ easing }}')),
            transition('active => removed', animate('{{ easeTime }}ms {{ easing }}')),
        ]),
    ]
})
export class ToastItem<Payload=any> implements OnDestroy {
    /** width of progress bar */
    width = signal(-1);

    /** a combination of toast type and options.toastClass */
    @HostBinding('class') toastClasses = '';

    state: WritableSignal<{
        value: 'inactive' | 'active' | 'removed';
        params: { easeTime: number | string; easing: string };
    }>;

    /** controls animation */
    @HostBinding('@flyInOut') get _state() {
        return this.state();
    }

    /** hides component when waiting to be displayed */
    @HostBinding('style.display')
    get displayStyle(): string | undefined {
        const has = this.state().value === 'inactive';
        return has ? 'none' : undefined;
    }

    private timeout: any;
    private intervalId: any;
    private hideTime!: number;
    private afterActivate: Subscription;
    private manualClosed: Subscription;
    private timeoutReset: Subscription;
    private countDuplicate: Subscription;

    options: MessageConfig<Payload>;
    duplicatesCount!: number;
    originalTimeout: number;
    title: string;
    summary: string;
    detail: string;
    closeIcon: string;
    contentIcon: string;

    constructor(
        protected service: ToastrService,
        public pkg: ToastPackage,
        public sanitizer: DomSanitizer,
        protected ngZone?: NgZone) {

        this.options = pkg.config;
        this.title = pkg.title;
        this.summary = pkg.message;
        this.detail = this.safeHtml(this.options.detail);
        this.closeIcon = this.extractCloseIcon();
        this.contentIcon = this.extractContentIcon();

        this.originalTimeout = pkg.config.timeOut;
        this.toastClasses = `${pkg.toastType} ${pkg.config.toastClass}`;

        this.afterActivate = pkg.toastRef.afterActivate().subscribe(() => {
            this.activateToast();
        });

        this.manualClosed = pkg.toastRef.manualClosed().subscribe(() => {
            this.remove();
        });

        this.timeoutReset = pkg.toastRef.timeoutReset().subscribe(() => {
            this.resetTimeout();
        });

        this.countDuplicate = pkg.toastRef.countDuplicate().subscribe(count => {
            this.duplicatesCount = count;
        });

        this.state = signal({
            value: 'inactive',
            params: {
                easeTime: this.pkg.config.easeTime,
                easing: 'ease-in',
            },
        });
    }

    ngOnDestroy() {
        this.afterActivate.unsubscribe();
        this.manualClosed.unsubscribe();
        this.timeoutReset.unsubscribe();
        this.countDuplicate.unsubscribe();
        clearInterval(this.intervalId);
        clearTimeout(this.timeout);
    }

    /**
     * activates toast and sets timeout
     */
    activateToast() {
        this.state.update(state => ({ ...state, value: 'active' }));
        const { disableTimeOut, timeOut, progressBar } = this.options;
        if (timeOut && !(disableTimeOut === true || disableTimeOut === 'timeOut')) {
            this.outsideTimeout(() => this.remove(), timeOut);
            this.hideTime = new Date().getTime() + timeOut;
            if (progressBar) this.outsideInterval(() => this.updateProgress(), 10);
        }
    }
    /**
     * updates progress bar width
     */
    updateProgress() {
        const width= this.width();
        const {timeOut} = this.options;

        if (width === 0 || width === 100 || !timeOut) {
            return;
        }
        const now = new Date().getTime();
        const remaining = this.hideTime - now;
        this.width.set((remaining /timeOut) * 100);

        if (this.width() <= 0) {
            this.width.set(0);
        }
        if (this.width() >= 100) {
            this.width.set(100);
        }
    }

    resetTimeout() {
        clearTimeout(this.timeout);
        clearInterval(this.intervalId);
        this.state.update(state => ({ ...state, value: 'active' }));

        this.outsideTimeout(() => this.remove(), this.originalTimeout);
        this.options.timeOut = this.originalTimeout;
        this.hideTime = new Date().getTime() + (this.options.timeOut || 0);
        this.width.set(-1);
        if (this.options.progressBar) {
            this.outsideInterval(() => this.updateProgress(), 10);
        }
    }

    /**
     * tells toastrService to remove this toast after animation time
     */
    remove() {
        if (this.state().value === 'removed') {
            return;
        }
        clearTimeout(this.timeout);
        this.state.update(state => ({ ...state, value: 'removed' }));
        this.outsideTimeout(
            () => this.service.remove(this.pkg.toastId),
            +this.pkg.config.easeTime,
        );
    }
    @HostListener('click')
    tapToast() {
        if (this.state().value === 'removed') {
            return;
        }
        this.pkg.triggerTap();
        if (this.options.tapToDismiss) {
            this.remove();
        }
    }
    @HostListener('mouseenter')
    stickAround() {
        if (this.state().value === 'removed') {
            return;
        }

        if (this.options.disableTimeOut !== 'extendedTimeOut') {
            clearTimeout(this.timeout);
            this.options.timeOut = 0;
            this.hideTime = 0;

            // disable progressBar
            clearInterval(this.intervalId);
            this.width.set(0);
        }
    }
    @HostListener('mouseleave')
    delayedHideToast() {
        if (
            (this.options.disableTimeOut === true || this.options.disableTimeOut === 'extendedTimeOut') ||
            this.options.extendedTimeOut === 0 ||
            this.state().value === 'removed'
        ) {
            return;
        }
        this.outsideTimeout(() => this.remove(), this.options.extendedTimeOut);
        this.options.timeOut = this.options.extendedTimeOut;
        this.hideTime = new Date().getTime() + (this.options.timeOut || 0);
        this.width.set(-1);
        if (this.options.progressBar) {
            this.outsideInterval(() => this.updateProgress(), 10);
        }
    }

    outsideTimeout(func: () => any, timeout: number) {
        if (this.ngZone) {
            this.ngZone.runOutsideAngular(
                () =>
                (this.timeout = setTimeout(
                    () => this.runInsideAngular(func),
                    timeout
                ))
            );
        } else {
            this.timeout = setTimeout(() => func(), timeout);
        }
    }

    outsideInterval(func: () => any, timeout: number) {
        if (this.ngZone) {
            this.ngZone.runOutsideAngular(
                () =>
                (this.intervalId = setInterval(
                    () => this.runInsideAngular(func),
                    timeout
                ))
            );
        } else {
            this.intervalId = setInterval(() => func(), timeout);
        }
    }

    private runInsideAngular(func: () => any) {
        if (this.ngZone) {
            this.ngZone.run(() => func());
        } else {
            func();
        }
    }

    private safeHtml(html: string): string {
        return html && this.options.enableHtml ? this.sanitizer.sanitize(SecurityContext.HTML, html) : null;
    }

    private extractCloseIcon(): string {
        return this.options.iconClasses?.closeIcon;
    }

    private extractContentIcon(): string {
        const {messageIcon, iconClasses, severity} = this.options;
        if(notBlank(messageIcon)) return messageIcon;
        else if(notBlank(severity) && notNull(iconClasses)) return iconClasses[severity];
        else return undefined;
    }
}