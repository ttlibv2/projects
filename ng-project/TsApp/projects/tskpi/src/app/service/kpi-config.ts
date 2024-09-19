import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

export interface IConfig {
    server_api?: string;
    excel_file?: string;
    excel_data?: string;
}

@Injectable({ providedIn: 'root' })
export class KpiConfig {
    readonly SESSION_KEY: string = "APP_CONFIG";
    constructor(public http: HttpClient) { }


    load(url?: string): Promise<IConfig> {
        return new Promise((resolve, reject) => {
            url = url || "assets/app.config.json";
            this.http.get<IConfig>(url).subscribe({
                next: data => { this.set(data); resolve(data) },
                error: msg => reject(msg)
            });
        });
    }

    set(cfig: IConfig): void {
        if (cfig != undefined) {
            cfig = Object.assign(this.get(), cfig);
            sessionStorage.setItem(this.SESSION_KEY, JSON.stringify(cfig));
        }
    }

    get(): IConfig {
        let cfig = sessionStorage.getItem(this.SESSION_KEY);
        return JSON.parse(cfig || '{}');
    }

    get excel_file(): string {
        return this.get()['excel_file'];
    }

    get server_api(): string {
        return this.get()['server_api'];
    }

    get excel_data(): string {
        return this.get()['excel_data'];
    }
}