import {LayoutConfig} from "ts-ui/app-layout";
import {TranslateLoader, TranslateModuleConfig} from "@ngx-translate/core";
import {HttpClient} from "@angular/common/http";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {ToastConfig} from "ts-ui/toast";
import {LocalDbConfig} from "ts-ui/local-db";
import {Template} from "./models/template";
import {Chanel} from "./models/chanel";
import {Software} from "./models/software";
import {GroupHelp} from "./models/group-help";
import {Question} from "./models/question";
import {Ticket} from "./models/ticket";
import * as od from "./models/od-cls";
import {AgTable} from "./models/ag-table";
import {ApiInfo} from "./models/api-info";
import {User} from "./models/user";
import {AppConfig} from "./models/app-config";
import {ConfigTable, DbAgTable, DbTable, DbUserTable, TB_NAMES, TemplateTable} from "./services/local-db.service";

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
export const authUri = '/signin';

export const AgTemplateCode = {
    agTicket: 'ticket_list'
};

export const layoutConfig: LayoutConfig = {
    visibleTopBar: false,
    visibleSideBar: false,
    visibleConfig: false,
    appLogo: 'assets/images/logo-dark.svg',
    appName: 'ConYeu.Vn',
    menuTheme: 'light',
    menuMode: 'slim',
    theme: 'aura-light',
    colorScheme: 'lime',
    themeUrlPrefix: '',

    listTheme: {
        'aura-light': {
            label: 'Aura Light',
            name: 'aura-light',
            colorSchemes: [
                {name: 'amber', color: '#f59e0b', url: 'aura-light-amber'},
                {name: 'blue', color: '#3B82F6', url: 'aura-light-blue'},
                {name: 'cyan', color: '#06b6d4', url: 'aura-light-cyan'},
                {name: 'green', color: '#10b981', url: 'aura-light-green'},
                {name: 'indigo', color: '#6366F1', url: 'aura-light-indigo'},
                {name: 'lime', color: '#84cc16', url: 'aura-light-lime'},
                {name: 'noir', color: '#020617', url: 'aura-light-noir'},
                {name: 'pink', color: '#ec4899', url: 'aura-light-pink'},
                {name: 'purple', color: '#8B5CF6', url: 'aura-light-purple'},
                {name: 'teal', color: '#14b8a6', url: 'aura-light-teal'},

            ]
        }
    },

    // listMenu2: [
    //   {
    //    label: 'Danh mục',
    //    items: [
    //       {label: 'Kênh - Tình trạng', icon: 'pi pi-address-book', routerLink: '/admin/chanels'},
    //       {label: 'Nhóm Phần mềm', icon: 'pi pi-android', routerLink: '/admin/softwares'},
    //       {label: 'Nhóm hỗ trợ', icon: 'pi pi-bookmark-fill', routerLink: '/admin/group-helps'},
    //       {label: 'Nội dung mẫu', icon: 'pi pi-chart-bar', routerLink: '/admin/questions'},
    //       {label: 'Danh mục TS', items: [
    //         {label: 'Support Team'},
    //         {label: 'Phân công cho'},
    //         {label: 'Ticket Subject Type'},
    //         {label: 'Replied Status'},
    //         {label: 'Danh mục'},
    //         {label: 'Danh mục phụ'},
    //         {label: 'Team Head'},
    //         {label: 'Loại yêu cầu hỗ trợ'},
    //         {label: 'Độ ưu tiên'},
    //         {label: 'Thẻ -- Tags'},
    //       ]}
    //    ]
    //  },

    // ]
};

export const toastConfig: Partial<ToastConfig> = {
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

export const databaseConfig: Partial<LocalDbConfig> = {
    dbName: 'ts_web',
    dbVersion: 2.0,
    schema: [
        {name: TB_NAMES.template, keyPath: "id", entityClass: Template, tableClass: TemplateTable},
        {name: TB_NAMES.chanel, keyPath: "id", entityClass: Chanel},
        {name: TB_NAMES.software, keyPath: 'id', entityClass: Software},
        {name: TB_NAMES.groupHelp, keyPath: 'id', entityClass: GroupHelp},
        {name: TB_NAMES.question, keyPath: 'id', entityClass: Question},
        {name: TB_NAMES.ticket, keyPath: 'id', entityClass: Ticket},
        {name: TB_NAMES.clsTeam, keyPath: 'id', entityClass: od.ClsTeam},
        {name: TB_NAMES.clsAssign, keyPath: 'id', entityClass: od.ClsAssign},
        {name: TB_NAMES.clsSubjectType, keyPath: 'id', entityClass: od.ClsSubjectType},
        {name: TB_NAMES.clsReplied, keyPath: 'id', entityClass: od.ClsRepiled},
        {name: TB_NAMES.clsCate, keyPath: 'id', entityClass: od.ClsCategory},
        {name: TB_NAMES.clsCateSub, keyPath: 'id', entityClass: od.ClsCategorySub},
        {name: TB_NAMES.clsTicketType, keyPath: 'id', entityClass: od.ClsTicketType},
        {name: TB_NAMES.clsPriority, keyPath: 'id', entityClass: od.ClsPriority},
        {name: TB_NAMES.clsProduct, keyPath: 'id', entityClass: od.ClsProduct},
        {name: TB_NAMES.clsStage, keyPath: 'id', entityClass: od.ClsStage},
        {name: TB_NAMES.clsTag, keyPath: 'id', entityClass: od.ClsTag},
        {name: TB_NAMES.clsTopic, keyPath: 'id', entityClass: od.ClsTopic},
        {name: TB_NAMES.agTable, keyPath: 'code', entityClass: AgTable, tableClass: DbAgTable},
        {name: TB_NAMES.apiInfo, keyPath: 'api_id', entityClass: ApiInfo},
        {name: TB_NAMES.user, keyPath: 'user_id', entityClass: User, tableClass: DbUserTable},
        {name: TB_NAMES.appConfig, keyPath: '', entityClass: AppConfig, tableClass: ConfigTable},
    ]
};