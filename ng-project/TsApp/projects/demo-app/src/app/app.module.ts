import {APP_INITIALIZER, InjectionToken, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoggerService, TsLoggerModule} from "ts-ui/logger";
import {DB_CONFIG_TOKEN, DBConfig, DBService} from "ts-ui/local-db";
import {Chanel} from "../../../ticketapp/src/app/models/chanel";


const databaseConfig: Partial<DBConfig> = {
    dbName: 'web_v2',
    dbVersion: 2.1,
    options: {autoOpen: true},
    schema: [
        {
            name: 'chanel',
            keyPath: 'chanel_id',
            entityClass: Chanel,
           // tableClass: ChanelTable,
            jsonToModel: json => Chanel.from(json)
        }
    ]

};

function INIT_DB(service: DBService, logger: LoggerService) {
    return () => service.initDatabase().subscribe({
        error: reason => logger.error(`Đã xảy ra lỗi khởi tạo database: `, reason),
        next: _ => logger.info(`Khởi tạo database thành công.`),
        complete: () => logger.log('database....')
    });
}



@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        TsLoggerModule.forRoot()
    ],
    providers: [
        {provide: DB_CONFIG_TOKEN, useValue: databaseConfig},
        {provide: APP_INITIALIZER, useFactory: INIT_DB, deps: [DBService, LoggerService], multi: true},
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}