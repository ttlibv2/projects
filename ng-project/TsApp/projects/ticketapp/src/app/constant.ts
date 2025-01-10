import { LayoutConfig } from "ts-ui/applayout";
import { TranslateLoader, TranslateModuleConfig } from "@ngx-translate/core";
import { HttpClient } from "@angular/common/http";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { ToastConfig } from "ts-ui/toast";

export const Urls = {
    signinUrl: '/signin',
    signupUrl: '/signup',
    template: '/templates',
    form_ticket: '/ticket-form',
    chanels: '/chanels',
    ghelp: '/ghelp',
    gsoft: '/gsoft',
    question: '/question'
};

export const HOME_PAGE = Urls.form_ticket;

export type Severity =
    'success'
    | 'info'
    | 'warn'
    | 'error'
    | 'help'
    | 'primary'
    | 'secondary'
    | 'contrast'
    | string
    | null
    | undefined;

export const AgTemplateCode = {
    agTicket: 'ticket_list'
};


export const layoutConfig: LayoutConfig = {
    visibleTopBar: false,
    visibleSideBar: false,
    visibleConfig: false,
    appLogo: '', // assets/images/logo-dark.svg
    appName: '', // ConYeu.Vn
    menuTheme: 'light',
    menuMode: 'static',
    theme: 'aura-light',
    colorScheme: 'lime',
    themeUrlPrefix: '',


    listTheme: {
        'aura-light': {
            label: 'Aura Light',
            name: 'aura-light',
            colorSchemes: [
                { name: 'amber', color: '#f59e0b', url: 'aura-light-amber' },
                { name: 'blue', color: '#3B82F6', url: 'aura-light-blue' },
                { name: 'cyan', color: '#06b6d4', url: 'aura-light-cyan' },
                { name: 'green', color: '#10b981', url: 'aura-light-green' },
                { name: 'indigo', color: '#6366F1', url: 'aura-light-indigo' },
                { name: 'lime', color: '#84cc16', url: 'aura-light-lime' },
                { name: 'noir', color: '#020617', url: 'aura-light-noir' },
                { name: 'pink', color: '#ec4899', url: 'aura-light-pink' },
                { name: 'purple', color: '#8B5CF6', url: 'aura-light-purple' },
                { name: 'teal', color: '#14b8a6', url: 'aura-light-teal' },

            ]
        }
    },

    listMenu: [
        {
            label: 'Ticket', icon: 'pi pi-ticket', items: [
                {
                    label: 'Quản lý vé',
                    icon: 'pi pi-ticket',
                    routerLink: ['/ticket-list'],
                    queryParams: { visible: false }
                },
                { label: 'Thêm mới vé', icon: 'pi pi-ticket', routerLink: ['/ticket-form'] },
                {
                    label: 'Quản lý email mẫu',
                    icon: 'pi pi-ticket',
                    routerLink: ['/templates'],
                    queryParams: { thread: 'email_template' }
                },
                {
                    label: 'Quản lý dl mẫu',
                    icon: 'pi pi-ticket',
                    routerLink: ['/templates'],
                    queryParams: { thread: 'ticket_template' }
                },
                { label: 'Quản lý lịch sử', icon: 'pi pi-ticket', routerLink: ['/ticket-history'] },
                { label: 'Quản lý tệp đính kèm', icon: 'pi pi-ticket', routerLink: ['/ticket-file'] },
                { label: 'Quản lý khách hàng', icon: 'pi pi-ticket', routerLink: ['/ticket-customer'] },
            ]
        },
        {
            label: 'Templates', icon: 'pi pi-ticket', items: [
                { label: 'Quản lý mẫu', icon: 'pi pi-ticket', routerLink: ['/templates'] },
            ]
        },
        {
            label: 'Danh mục', icon: 'pi pi-ticket', expanded: false, items: [
                { label: 'Kênh - Tình trạng', icon: 'pi pi-ticket', routerLink: ['/chanel'] },
                { label: 'Nhóm phần mềm', icon: 'pi pi-ticket', routerLink: ['/software'] },
                { label: 'Nhóm câu hỏi', icon: 'pi pi-ticket', routerLink: ['/question'] },
                { label: 'Nhóm hỗ trợ', icon: 'pi pi-ticket', routerLink: ['/ghelp'] },
            ]
        },
        {
            label: '[TS] Danh mục', icon: 'pi pi-ticket', items: [
                { label: 'Support Team', icon: 'pi pi-ticket', routerLink: ['/ts-steam'] },
                { label: 'Danh mục', icon: 'pi pi-ticket', routerLink: ['/ts-category'] },
                { label: 'Danh mục phụ', icon: 'pi pi-ticket', routerLink: ['/ts-sub-category'] },
                { label: 'Ticket type', icon: 'pi pi-ticket', routerLink: ['/ts-ticket-type'] },
                { label: 'Ticket Subject Type', icon: 'pi pi-ticket', routerLink: ['/ts-ticket-subject-type'] },
                { label: 'Replied Status', icon: 'pi pi-ticket', routerLink: ['/ts-replied-status'] },
                { label: 'Độ ưu tiên', icon: 'pi pi-ticket', routerLink: ['/ts-priority'] },
                { label: 'Thẻ -- Tags', icon: 'pi pi-ticket', routerLink: ['/ts-tag'] },
                { label: 'Team Head', icon: 'pi pi-ticket', routerLink: ['/ts-team-head'] },
            ]
        }
    ]
};

//const position = !!inject(Platform).ANDROID ? 'bottom-right' : undefined;

export const toastConfig: Partial<ToastConfig> = {
    newestOnTop: true,
    position: 'top-right'
    // toastClassPrefix: 'p-toast',
    // closeClass: 'close-icon',
    // autoDismiss: true,
    // closeButton: true,
    // enableHtml: true,
    // newestOnTop: true,
    // tapToDismiss: true,
    // disableTimeOut: false,
    // toastComponent: ToastItem,
    // progressBar: false,

    // position: 'top-right',

};

export const translateConfig: TranslateModuleConfig = {
    defaultLanguage: 'vi',
    loader: {
        provide: TranslateLoader,
        deps: [HttpClient],
        useFactory: (http: HttpClient) => new TranslateHttpLoader(http, './assets/i18n/', '.json'),
    }
}